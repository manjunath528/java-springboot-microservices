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

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(summary = "Get Users")
  public ResponseEntity<List<UserResponseDTO>> getUsers() {
    List<UserResponseDTO> users = userService.getUsers();
    return ResponseEntity.ok().body(users);
  }

  @PostMapping
  @Operation(summary = "Create a new User")
  public ResponseEntity<UserResponseDTO> createUser(
      @Validated({Default.class, CreateUserValidationGroup.class})
      @RequestBody UserRequestDTO userRequestDTO) {

    UserResponseDTO userResponseDTO = userService.createUser(
            userRequestDTO);
    return ResponseEntity.ok().body(userResponseDTO);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a new User")
  public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id,
      @Validated({Default.class}) @RequestBody UserRequestDTO userRequestDTO) {
    UUID uuid = UUID.fromString(id);
    UserResponseDTO userResponseDTO = userService.updateUser(uuid,
        userRequestDTO);
    return ResponseEntity.ok().body(userResponseDTO);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a User")
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    UUID uuid = UUID.fromString(id);
    userService.deleteUser(uuid);
    return ResponseEntity.noContent().build();
  }
}
