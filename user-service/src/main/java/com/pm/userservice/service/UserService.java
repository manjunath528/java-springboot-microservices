package com.pm.userservice.service;

import com.pm.userservice.dto.UserRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.exception.EmailAlreadyExistsException;
import com.pm.userservice.exception.UserNotFoundException;
import com.pm.userservice.grpc.BillingServiceGrpcClient;
import com.pm.userservice.kafka.KafkaProducer;
import com.pm.userservice.mapper.UserMapper;
import com.pm.userservice.model.User;
import com.pm.userservice.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository UserRepository;
  private final BillingServiceGrpcClient billingServiceGrpcClient;
  private final KafkaProducer kafkaProducer;

  public UserService(UserRepository UserRepository,
                     BillingServiceGrpcClient billingServiceGrpcClient,
                     KafkaProducer kafkaProducer) {
    this.UserRepository = UserRepository;
    this.billingServiceGrpcClient = billingServiceGrpcClient;
    this.kafkaProducer = kafkaProducer;
  }

  public List<UserResponseDTO> getUsers() {
    List<User> users = UserRepository.findAll();
    return users.stream().map(UserMapper::toDTO).toList();
  }

  public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
    if (UserRepository.existsByEmail(userRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException(
          "A user with this email " + "already exists"
              + userRequestDTO.getEmail());
    }

    User newUser = UserRepository.save(
        UserMapper.toModel(userRequestDTO));
    billingServiceGrpcClient.createBillingAccount(newUser.getId().toString(),
        newUser.getName(), newUser.getEmail());

    kafkaProducer.sendEvent(newUser);

    return UserMapper.toDTO(newUser);
  }

  public UserResponseDTO updateUser(UUID id,
      UserRequestDTO userRequestDTO) {

    User user = UserRepository.findById(id).orElseThrow(
        () -> new UserNotFoundException("user not found with ID: " + id));

    if (UserRepository.existsByEmailAndIdNot(userRequestDTO.getEmail(),
        id)) {
      throw new EmailAlreadyExistsException(
          "A user with this email " + "already exists"
              + userRequestDTO.getEmail());
    }

    user.setName(userRequestDTO.getName());
    user.setAddress(userRequestDTO.getAddress());
    user.setEmail(userRequestDTO.getEmail());
    user.setDateOfBirth(LocalDate.parse(userRequestDTO.getDateOfBirth()));

    User updatedUser = UserRepository.save(user);
    return UserMapper.toDTO(updatedUser);
  }

  public void deleteUser(UUID id) {
    UserRepository.deleteById(id);
  }
}
