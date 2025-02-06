package com.foodtaste.service;

import java.util.List;

import com.foodtaste.dto.UserDTO;
import com.foodtaste.model.User;

public interface UserService {

	UserDTO createUser(User user);

	List<UserDTO> getAllUsers();

	UserDTO getUserById(Integer id);

	UserDTO updateUser(Integer id, User updatedUser);

	void deleteUser(Integer id);
}
