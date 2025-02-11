package com.foodtaste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodtaste.constant.AppConstants;
import com.foodtaste.dto.UserDTO;
import com.foodtaste.model.User;
import com.foodtaste.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(AppConstants.APP_NAME + AppConstants.PRIVATE_ROUTE_TYPE + "/user")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@PutMapping(AppConstants.UPDATE + "/{id}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @Valid @RequestBody User user) {
		log.info("Received request to update user with ID: {}", id);

		UserDTO updatedUser = userService.updateUser(id, user);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping(AppConstants.DELETE + "/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable String id) {
		log.info("Received request to delete user with ID: {}", id);

		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
