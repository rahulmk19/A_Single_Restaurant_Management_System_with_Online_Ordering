package com.foodtaste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodtaste.dto.UserDTO;
import com.foodtaste.model.User;
import com.foodtaste.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PublicController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<UserDTO> createNewUser(@Valid @RequestBody User user) {
		log.info("Received request to create user: {}", user);

		UserDTO createdUser = userService.createUser(user);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}
}
