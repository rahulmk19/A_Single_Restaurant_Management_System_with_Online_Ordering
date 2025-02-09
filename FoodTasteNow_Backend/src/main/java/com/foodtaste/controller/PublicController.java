package com.foodtaste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodtaste.dto.UserRequest;
import com.foodtaste.service.PublicAuthService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class PublicController {

	@Autowired
	private PublicAuthService publicAuthService;

	@PostMapping("/register")
	public ResponseEntity<String> createNewUser(@Valid @RequestBody UserRequest userRequest) {
		log.info("Received request to create user: {}", userRequest);

		String createdUser = publicAuthService.register(userRequest);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@Valid @RequestBody UserRequest userRequest) {
		log.info("Received login request for email: {}", userRequest.getEmail());
		
		String jwtToken = publicAuthService.login(userRequest);
		return new ResponseEntity<>(jwtToken, HttpStatus.OK);
	}

}
