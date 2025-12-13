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
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final Logger logger = LoggerFactory.getLogger(UserService.class);
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

  public List<UserResponseDTO> getUsers() {
    List<User> users = userRepository.findAll();
    return users.stream().map(UserMapper::toDTO).toList();
  }

  public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
    if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException(
          "A user with this email " + "already exists"
              + userRequestDTO.getEmail());
    }

    User newUser = userRepository.save(
        UserMapper.toModel(userRequestDTO));
    billingServiceGrpcClient.createBillingAccount(newUser.getId().toString(),
        newUser.getName(), newUser.getEmail());
    kafkaProducer.sendEvent(newUser);
    return UserMapper.toDTO(newUser);
  }
  public UserResponseDTO updateUser(UUID id,
      UserRequestDTO userRequestDTO) {
    logger.info("Requested UserId for update->{}",id);
    Optional<User>  optionalUser = userRepository.findById(id);
    logger.info("User found in DB: {}", optionalUser.isPresent());
    User user = optionalUser.orElseThrow(
        () -> new UserNotFoundException("user not found with ID: " + id));
    if (userRepository.existsByEmailAndIdNot(userRequestDTO.getEmail(),
        id)) {
      throw new EmailAlreadyExistsException(
          "A user with this email " + "already exists"
              + userRequestDTO.getEmail());
    }

    user.setName(userRequestDTO.getName());
    user.setAddress(userRequestDTO.getAddress());
    user.setEmail(userRequestDTO.getEmail());
    user.setDateOfBirth(LocalDate.parse(userRequestDTO.getDateOfBirth()));
    User updatedUser = userRepository.save(user);
    return UserMapper.toDTO(updatedUser);
  }

  public void deleteUser(UUID id) {
    userRepository.deleteById(id);
  }
}
