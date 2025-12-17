package com.pm.userservice.controller;

import com.pm.userservice.dto.UserRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.dto.validators.CreateUserValidationGroup;
import com.pm.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "API for managing Users")
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(summary = "Get Users")
  public ResponseEntity<List<UserResponseDTO>> getUsers() {
    log.info("Request received -> Get all users");

    List<UserResponseDTO> users = userService.getUsers();

    log.info("Response sent -> Total users fetched -> {}", users.size());
    return ResponseEntity.ok().body(users);
  }

  @PostMapping
  @Operation(summary = "Create a new User")
  public ResponseEntity<UserResponseDTO> createUser(
          @Validated({Default.class, CreateUserValidationGroup.class})
          @RequestBody UserRequestDTO userRequestDTO) {

    log.info("Request received -> Create user -> name->{} email->{}",
            userRequestDTO.getName(),
            userRequestDTO.getEmail());

    UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);

    log.info("User created successfully -> userId->{}",
            userResponseDTO.getId());

    return ResponseEntity.ok().body(userResponseDTO);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get User Details by ID")
  public ResponseEntity<UserResponseDTO> getUserDetails(@PathVariable String id) {
    log.info("Request received -> Get user details -> userId->{}", id);

    UUID uuid = UUID.fromString(id);
    UserResponseDTO userResponseDTO = userService.getUserById(uuid);

    log.info("User details fetched successfully -> userId->{}", id);
    return ResponseEntity.ok(userResponseDTO);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a new User")
  public ResponseEntity<UserResponseDTO> updateUser(
          @PathVariable String id,
          @Validated({Default.class}) @RequestBody UserRequestDTO userRequestDTO) {

    log.info("Request received -> Update user -> userId->{}", id);

    UUID uuid = UUID.fromString(id);
    UserResponseDTO userResponseDTO =
            userService.updateUser(uuid, userRequestDTO);

    log.info("User updated successfully -> userId->{}", id);
    return ResponseEntity.ok().body(userResponseDTO);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a User")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    log.info("Request received -> Delete user -> userId->{}", id);

    UUID uuid = UUID.fromString(id);
    userService.deleteUser(uuid);

    log.info("User deleted successfully -> userId->{}", id);
    return ResponseEntity.noContent().build();
  }
}
