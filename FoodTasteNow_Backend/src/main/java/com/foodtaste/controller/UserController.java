package com.foodtaste.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodtaste.constant.AppConstants;
import com.foodtaste.dto.Profile;
import com.foodtaste.dto.UserRequestDTO;
import com.foodtaste.dto.UserResponseDTO;
import com.foodtaste.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(AppConstants.APP_NAME + "/users")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@PostConstruct
	public void initRoleAndUsers() {
		userService.initRoleAndUsers();
	}

	@PutMapping(AppConstants.USER + AppConstants.UPDATE + "/{id}")
	public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") Long userId,
			@Valid @RequestBody UserRequestDTO userRequestDTO) {
		log.info("Received request to update user with ID: {}", userId);

		UserResponseDTO updatedUser = userService.updateUser(userId, userRequestDTO);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}

	@GetMapping(AppConstants.COMMON + "/profile")
	public ResponseEntity<Profile> getProfile() {
		log.info("Received request to fetch Profile of users");

		Profile profile = userService.getUserProfile();
		return ResponseEntity.ok(profile);
	}

	@PatchMapping(AppConstants.ADMIN + "/changeRole")
	public ResponseEntity<UserResponseDTO> changeUserRole(@RequestParam Long userId, @RequestParam String roleName) {
		log.info("Changing role of user {} to {}", userId, roleName);
		UserResponseDTO updatedUser = userService.convertUserToAdmin(userId, roleName);
		return ResponseEntity.ok(updatedUser);
	}

	@GetMapping(AppConstants.ADMIN + AppConstants.GET_ALL)
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		log.info("Received request to fetch all users");

		List<UserResponseDTO> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping(AppConstants.ADMIN + AppConstants.GET_BY_ID + "/{id}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
		log.info("Received request to fetch user with ID: {}", userId);

		UserResponseDTO user = userService.getUserById(userId);
		return ResponseEntity.ok(user);
	}

	@DeleteMapping(AppConstants.ADMIN + AppConstants.DELETE + "/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
		log.info("Received request to delete user with ID: {}", userId);

		String userDeleted = userService.deleteUser(userId);
		return new ResponseEntity<>(userDeleted, HttpStatus.ACCEPTED);
	}

}
