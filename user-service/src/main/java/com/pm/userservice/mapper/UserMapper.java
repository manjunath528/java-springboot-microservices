package com.pm.userservice.mapper;

import com.pm.userservice.dto.UserRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.model.User;

import java.time.LocalDate;

public class UserMapper {
  public static UserResponseDTO toDTO(User user) {
    UserResponseDTO userDTO = new UserResponseDTO();
    userDTO.setId(user.getId().toString());
    userDTO.setName(user.getName());
    userDTO.setAddress(user.getAddress());
    userDTO.setEmail(user.getEmail());
    userDTO.setDateOfBirth(user.getDateOfBirth().toString());

    return userDTO;
  }

  public static User toModel(UserRequestDTO UserRequestDTO) {
    User user = new User();
    user.setName(UserRequestDTO.getName());
    user.setAddress(UserRequestDTO.getAddress());
    user.setEmail(UserRequestDTO.getEmail());
    user.setDateOfBirth(LocalDate.parse(UserRequestDTO.getDateOfBirth()));
    user.setRegisteredDate(LocalDate.parse(UserRequestDTO.getRegisteredDate()));
    return user;
  }
}
