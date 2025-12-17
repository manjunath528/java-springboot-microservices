package com.pm.userservice.service;

import com.pm.userservice.dto.UserRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.exception.EmailAlreadyExistsException;
import com.pm.userservice.exception.UserNotFoundException;
import com.pm.userservice.kafka.KafkaProducer;
import com.pm.userservice.mapper.UserMapper;
import com.pm.userservice.model.User;
import com.pm.userservice.repository.UserRepository;
import com.pm.userservice.grpc.BillingServiceGrpcClient;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import user.events.UserEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final BillingServiceGrpcClient billingServiceGrpcClient;
  private final KafkaProducer kafkaProducer;

  public UserService(UserRepository userRepository,
                     BillingServiceGrpcClient billingServiceGrpcClient,
                     KafkaProducer kafkaProducer) {
    this.userRepository = userRepository;
    this.billingServiceGrpcClient = billingServiceGrpcClient;
    this.kafkaProducer = kafkaProducer;
  }

  // ================== GET ALL USERS ==================
  public List<UserResponseDTO> getUsers() {
    List<User> users = userRepository.findAll();
    logger.info("Fetched->{} users from database", users.size());
    return users.stream()
            .map(UserMapper::toDTO)
            .toList();
  }

  // ================== CREATE USER ==================
  public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
    if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException(
              "A user with email " + userRequestDTO.getEmail() + " already exists");
    }


    User newUser = UserMapper.toEntity(userRequestDTO);
    // Save user in DB
    userRepository.save(newUser);

    newUser = userRepository.save(newUser);

    // Create billing account via gRPC
    billingServiceGrpcClient.createBillingAccount(
            newUser.getId().toString(),
            newUser.getName(),
            newUser.getEmail()
    );

    // Send Kafka event
    kafkaProducer.sendEvent(newUser);

    logger.info("Created new user->{}", newUser.getId());
    return UserMapper.toDTO(newUser);
  }

  // ================== UPDATE USER ==================
  @Transactional
  public UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO) {
    logger.info("Requested update for UserId->{}", id);

    Optional<User> optionalUser = userRepository.findById(id);
    User user = optionalUser.orElseThrow(
            () -> new UserNotFoundException("User not found with ID: " + id));

    logger.info("User found in DB: {}", user.getId());

    // Check for duplicate email
    if (userRepository.existsByEmailAndIdNot(userRequestDTO.getEmail(), id)) {
      throw new EmailAlreadyExistsException(
              "A user with email " + userRequestDTO.getEmail() + " already exists");
    }

    // Update fields
    user.setName(userRequestDTO.getName());
    user.setAddress(userRequestDTO.getAddress());
    user.setEmail(userRequestDTO.getEmail());
    user.setWeight(userRequestDTO.getWeight());
    user.setHeight(userRequestDTO.getHeight());
    user.setGender(userRequestDTO.getGender());
    user.setFitnessGoal(userRequestDTO.getFitnessGoal());
    user.setDateOfBirth(LocalDate.parse(userRequestDTO.getDateOfBirth()));
    user.setDailyStepGoal(userRequestDTO.getDailyStepGoal());
    user.setSleepGoalHours(userRequestDTO.getSleepGoalHours());
    user.setNotificationsEnabled(userRequestDTO.getNotificationsEnabled());

    // Save updates
    User updatedUser = userRepository.save(user);

    // Optional: Publish USER_UPDATED Kafka event
    kafkaProducer.sendEvent(updatedUser);

    logger.info("Updated user successfully->{}", updatedUser.getId());
    return UserMapper.toDTO(updatedUser);
  }

  public UserResponseDTO getUserById(UUID id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

    logger.info("Fetched user details for ID->{}", id);
    return UserMapper.toDTO(user);
  }

  // ================== DELETE USER ==================
  public void deleteUser(UUID id) {
    logger.info("User details with ID->{}", id);
    User user = userRepository.findById(id)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found with ID: " + id)
            );
    userRepository.deleteById(id);
    logger.info("Deleted user with ID->{}", id);
    kafkaProducer.sendUserDeletedEvent(user);
    logger.info("Successfully deleted user with ID->{}", id);
  }
}
