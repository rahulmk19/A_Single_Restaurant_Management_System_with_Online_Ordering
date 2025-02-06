package com.foodtaste.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodtaste.dto.UserDTO;
import com.foodtaste.enums.Role;
import com.foodtaste.exception.UserException;
import com.foodtaste.model.User;
import com.foodtaste.repository.UserRepository;
import com.foodtaste.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDTO createUser(User user) {
		log.info("Creating user with email: {} and mobile: {}", user.getEmail(), user.getMobileNumber());
		if (userRepository.existsByEmail(user.getEmail())) {
			log.warn("Email already exists: {}", user.getEmail());
			throw new UserException("Email already exists");
		}
		if (userRepository.existsByMobileNumber(user.getMobileNumber())) {
			log.warn("Mobile number already exists: {}", user.getMobileNumber());
			throw new UserException("Mobile number already exists");
		}

		if (user.getEmail().endsWith("@foodtaste.com")) {
			user.setRole(Role.ADMIN);
		} else {
			user.setRole(Role.USER);
		}
		User savedUser = userRepository.save(user);
		log.info("User created successfully with ID: {}", savedUser.getId());
		return modelMapper.map(savedUser, UserDTO.class);
	}

	@Override
	public List<UserDTO> getAllUsers() {
		log.info("Fetching all users from the database");
		List<User> allUsers = userRepository.findAll();
		return allUsers.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
	}

	@Override
	public UserDTO getUserById(Integer id) {
		log.info("Fetching user with ID: {}", id);
		User user = userRepository.findById(id).orElseThrow(() -> {
			log.warn("User not found with ID: {}", id);
			return new UserException("User not found with ID: " + id);
		});
		return modelMapper.map(user, UserDTO.class);
	}

	@Override
	public UserDTO updateUser(Integer id, User updatedUser) {
		log.info("Updating user with ID: {}", id);
		User user = userRepository.findById(id).orElseThrow(() -> {
			log.warn("User not found with ID: {}", id);
			return new UserException("User not found with ID: " + id);
		});

		if (userRepository.existsByEmail(updatedUser.getEmail()) && !user.getEmail().equals(updatedUser.getEmail())) {
			log.warn("Email conflict for user update: {}", updatedUser.getEmail());
			throw new UserException("Email already exists");
		}

		if (userRepository.existsByMobileNumber(updatedUser.getMobileNumber())
				&& !user.getMobileNumber().equals(updatedUser.getMobileNumber())) {
			log.warn("Mobile number conflict for user update: {}", updatedUser.getMobileNumber());
			throw new UserException("Mobile number already exists");
		}

		user.setName(updatedUser.getName());
		user.setPassword(updatedUser.getPassword());
		user.setEmail(updatedUser.getEmail());
		user.setMobileNumber(updatedUser.getMobileNumber());

		User savedUser = userRepository.save(user);
		log.info("User updated successfully with ID: {}", savedUser.getId());

		return modelMapper.map(savedUser, UserDTO.class);
	}

	@Override
	public void deleteUser(Integer id) {
		log.info("Deleting user with ID: {}", id);
		User user = userRepository.findById(id).orElseThrow(() -> {
			log.warn("User not found with ID: {}", id);
			return new UserException("User not found with ID: " + id);
		});
		userRepository.delete(user);
		log.info("User deleted successfully with ID: {}", id);
	}
}
