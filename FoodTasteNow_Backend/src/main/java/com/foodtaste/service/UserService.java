package com.foodtaste.service;

import java.util.List;

import com.foodtaste.dto.Profile;
import com.foodtaste.dto.UserRequestDTO;
import com.foodtaste.dto.UserResponseDTO;

public interface UserService {

	void initRoleAndUsers();

	UserResponseDTO createNewUser(UserRequestDTO userRequestDTO);

	List<UserResponseDTO> getAllUsers();

	UserResponseDTO getUserById(Long userId);

	UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO);

	String deleteUser(Long userId);

	UserResponseDTO convertUserToAdmin(Long userId, String roleName);

	Profile getUserProfile();

}
