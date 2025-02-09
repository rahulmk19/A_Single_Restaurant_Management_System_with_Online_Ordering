package com.foodtaste.service;

import java.util.List;
import java.util.Optional;

import com.foodtaste.dto.UserDTO;
import com.foodtaste.model.User;

public interface UserService {

	UserDTO createUser(User user);

	List<UserDTO> getAllUsers();

	UserDTO getUserById(String uuid);

	Optional<User> findByEmail(String email);

	UserDTO updateUser(String uuid, User updatedUser);

	void deleteUser(String uuid);
	
}
