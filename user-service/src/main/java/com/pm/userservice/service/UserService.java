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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

  private static final Logger logger =
          LoggerFactory.getLogger(UserService.class);

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
    logger.info("Fetching all users");
    List<User> users = userRepository.findAll();
    logger.info("Fetched->{} users", users.size());

    return users.stream()
            .map(UserMapper::toDTO)
            .toList();
  }

  // ================== CREATE USER ==================
  public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

    logger.info("Creating user with email->{}",
            userRequestDTO.getEmail());

    if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
      logger.warn("Email already exists->{}",
              userRequestDTO.getEmail());
      throw new EmailAlreadyExistsException(
              "User with email " + userRequestDTO.getEmail() + " already exists"
      );
    }

    User user = UserMapper.toEntity(userRequestDTO);
    User savedUser = userRepository.save(user);

    logger.info("User saved in DB with ID->{}", savedUser.getId());

    // Create billing account
    billingServiceGrpcClient.createBillingAccount(
            savedUser.getId().toString(),
            savedUser.getName(),
            savedUser.getEmail()
    );

    logger.info("Billing account created for userId->{}",
            savedUser.getId());

    // Publish Kafka event
    kafkaProducer.sendEvent(savedUser);
    logger.info("User created event published for userId->{}",
            savedUser.getId());

    return UserMapper.toDTO(savedUser);
  }

  // ================== UPDATE USER ==================
  @Transactional
  public UserResponseDTO updateUser(UUID id,
                                    UserRequestDTO userRequestDTO) {

    logger.info("Updating userId->{}", id);

    User user = userRepository.findById(id)
            .orElseThrow(() -> {
              logger.warn("User not found for update->{}", id);
              return new UserNotFoundException(
                      "User not found with ID: " + id
              );
            });

    if (userRepository.existsByEmailAndIdNot(
            userRequestDTO.getEmail(), id)) {

      logger.warn("Duplicate email detected during update->{}",
              userRequestDTO.getEmail());

      throw new EmailAlreadyExistsException(
              "User with email " + userRequestDTO.getEmail() + " already exists"
      );
    }

    // Update fields
    user.setName(userRequestDTO.getName());
    user.setEmail(userRequestDTO.getEmail());
    user.setAddress(userRequestDTO.getAddress());
    user.setWeight(userRequestDTO.getWeight());
    user.setHeight(userRequestDTO.getHeight());
    user.setGender(userRequestDTO.getGender());
    user.setFitnessGoal(userRequestDTO.getFitnessGoal());
    user.setDateOfBirth(LocalDate.parse(userRequestDTO.getDateOfBirth()));
    user.setDailyStepGoal(userRequestDTO.getDailyStepGoal());
    user.setSleepGoalHours(userRequestDTO.getSleepGoalHours());
    user.setNotificationsEnabled(userRequestDTO.getNotificationsEnabled());

    User updatedUser = userRepository.save(user);

    kafkaProducer.sendEvent(updatedUser);
    logger.info("User updated successfully->{}", updatedUser.getId());

    return UserMapper.toDTO(updatedUser);
  }

  // ================== GET USER BY ID ==================
  public UserResponseDTO getUserById(UUID id) {

    logger.info("Fetching user by ID->{}", id);

    User user = userRepository.findById(id)
            .orElseThrow(() -> {
              logger.warn("User not found->{}", id);
              return new UserNotFoundException(
                      "User not found with ID: " + id
              );
            });

    return UserMapper.toDTO(user);
  }

  // ================== DELETE USER ==================
  public void deleteUser(UUID id) {

    logger.info("Deleting userId->{}", id);

    User user = userRepository.findById(id)
            .orElseThrow(() -> {
              logger.warn("User not found for deletion->{}", id);
              return new UserNotFoundException(
                      "User not found with ID: " + id
              );
            });

    userRepository.delete(user);
    logger.info("User deleted from DB->{}", id);

    kafkaProducer.sendUserDeletedEvent(user);
    logger.info("User deleted event published for userId->{}", id);
  }
}
