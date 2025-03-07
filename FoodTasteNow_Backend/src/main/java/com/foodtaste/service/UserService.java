package com.foodtaste.service;

import java.util.List;
import java.util.Optional;

import com.foodtaste.dto.UserRequestDTO;
import com.foodtaste.dto.UserResponseDTO;
import com.foodtaste.model.User;

public interface UserService {

	void initRoleAndUsers();

	UserResponseDTO createNewUser(UserRequestDTO userRequestDTO);

	List<UserResponseDTO> getAllUsers();

	UserResponseDTO getUserById(Long userId);

	UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO);

	String deleteUser(Long userId);

}
