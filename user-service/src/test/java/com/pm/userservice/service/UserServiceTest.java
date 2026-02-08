package com.pm.userservice.service;

import com.pm.userservice.dto.UserRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.exception.EmailAlreadyExistsException;
import com.pm.userservice.exception.UserNotFoundException;
import com.pm.userservice.grpc.BillingServiceGrpcClient;
import com.pm.userservice.kafka.KafkaProducer;
import com.pm.userservice.model.FitnessGoal;
import com.pm.userservice.model.Gender;
import com.pm.userservice.model.User;
import com.pm.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BillingServiceGrpcClient billingServiceGrpcClient;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private UserService userService;

    @Test
    void getUsersReturnsMappedDtos() {
        User user1 = sampleUser(UUID.randomUUID(), "Alex", "alex@example.com");
        User user2 = sampleUser(UUID.randomUUID(), "Jamie", "jamie@example.com");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDTO> result = userService.getUsers();

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getEmail()).isEqualTo("alex@example.com");
        assertThat(result.getLast().getName()).isEqualTo("Jamie");
    }

    @Test
    void createUserThrowsWhenEmailExists() {
        UserRequestDTO request = sampleRequest("Alex", "alex@example.com");
        when(userRepository.existsByEmail("alex@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
        verify(billingServiceGrpcClient, never()).createBillingAccount(any(), any(), any());
        verify(kafkaProducer, never()).sendEvent(any());
    }

    @Test
    void createUserPersistsUserAndPublishesEvents() {
        UserRequestDTO request = sampleRequest("Alex", "alex@example.com");
        when(userRepository.existsByEmail("alex@example.com")).thenReturn(false);

        User saved = sampleUser(UUID.randomUUID(), "Alex", "alex@example.com");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserResponseDTO response = userService.createUser(request);

        assertThat(response.getId()).isEqualTo(saved.getId().toString());
        assertThat(response.getEmail()).isEqualTo("alex@example.com");

        verify(billingServiceGrpcClient)
                .createBillingAccount(saved.getId().toString(), saved.getName(), saved.getEmail());
        verify(kafkaProducer).sendEvent(saved);
    }

    @Test
    void updateUserThrowsWhenNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(userId, sampleRequest("Alex", "alex@example.com")))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateUserThrowsWhenDuplicateEmail() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(sampleUser(userId, "Alex", "alex@example.com")));
        when(userRepository.existsByEmailAndIdNot("alex@example.com", userId)).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(userId, sampleRequest("Alex", "alex@example.com")))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
        verify(kafkaProducer, never()).sendEvent(any());
    }

    @Test
    void updateUserUpdatesFieldsAndPublishesEvent() {
        UUID userId = UUID.randomUUID();
        User existing = sampleUser(userId, "Old", "old@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmailAndIdNot("new@example.com", userId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserRequestDTO request = sampleRequest("New Name", "new@example.com");
        request.setAddress("New Address");
        request.setWeight(78.0);
        request.setHeight(1.75);
        request.setGender(Gender.OTHER);
        request.setFitnessGoal(FitnessGoal.IMPROVE_SLEEP);
        request.setDateOfBirth("1995-02-15");
        request.setDailyStepGoal(9000);
        request.setSleepGoalHours(7.0);
        request.setNotificationsEnabled(false);

        UserResponseDTO response = userService.updateUser(userId, request);

        assertThat(response.getName()).isEqualTo("New Name");
        assertThat(response.getEmail()).isEqualTo("new@example.com");
        assertThat(existing.getDateOfBirth()).isEqualTo(LocalDate.parse("1995-02-15"));
        assertThat(existing.getFitnessGoal()).isEqualTo(FitnessGoal.IMPROVE_SLEEP);

        verify(kafkaProducer).sendEvent(existing);
    }

    @Test
    void getUserByIdThrowsWhenNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getUserByIdReturnsMappedDto() {
        UUID userId = UUID.randomUUID();
        User user = sampleUser(userId, "Alex", "alex@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserById(userId);

        assertThat(response.getId()).isEqualTo(userId.toString());
        assertThat(response.getEmail()).isEqualTo("alex@example.com");
    }

    @Test
    void deleteUserThrowsWhenNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(kafkaProducer, never()).sendUserDeletedEvent(any());
    }

    @Test
    void deleteUserRemovesUserAndPublishesDeleteEvent() {
        UUID userId = UUID.randomUUID();
        User user = sampleUser(userId, "Alex", "alex@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).delete(user);
        verify(kafkaProducer).sendUserDeletedEvent(user);
    }

    private static UserRequestDTO sampleRequest(String name, String email) {
        UserRequestDTO request = new UserRequestDTO();
        request.setName(name);
        request.setEmail(email);
        request.setAddress("123 Main St");
        request.setWeight(70.0);
        request.setHeight(1.72);
        request.setGender(Gender.MALE);
        request.setDateOfBirth("1990-01-01");
        request.setFitnessGoal(FitnessGoal.GENERAL_FITNESS);
        request.setDailyStepGoal(8000);
        request.setSleepGoalHours(8.0);
        request.setNotificationsEnabled(true);
        return request;
    }

    private static User sampleUser(UUID id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setAddress("123 Main St");
        user.setWeight(70.0);
        user.setHeight(1.72);
        user.setGender(Gender.MALE);
        user.setDateOfBirth(LocalDate.parse("1990-01-01"));
        user.setFitnessGoal(FitnessGoal.GENERAL_FITNESS);
        user.setDailyStepGoal(8000);
        user.setSleepGoalHours(8.0);
        user.setNotificationsEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
