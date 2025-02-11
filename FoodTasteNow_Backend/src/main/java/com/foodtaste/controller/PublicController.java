package com.foodtaste.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodtaste.constant.AppConstants;
import com.foodtaste.dto.LoginRequest;
import com.foodtaste.dto.UserRequest;
import com.foodtaste.service.PublicAuthService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(AppConstants.APP_NAME + AppConstants.Auth)
@Slf4j
@CrossOrigin(origins = "*")
public class PublicController {

	@Autowired
	private PublicAuthService publicAuthService;

	@PostMapping("/register")
	public ResponseEntity<UserRequest> createNewUser(@Valid @RequestBody UserRequest userRequest) {
		log.info("Received request to create user: {}", userRequest);

		UserRequest createdUser = publicAuthService.register(userRequest);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("Received login request for email: {}", loginRequest.getEmail());

		String jwtToken = publicAuthService.login(loginRequest);
		log.info("Generated JWT Token: {}", jwtToken);
		
		if (jwtToken == null || jwtToken.isEmpty()) {
	        return new ResponseEntity<>("Token generation failed", HttpStatus.UNAUTHORIZED);
	    }
		return new ResponseEntity<>(jwtToken, HttpStatus.OK);
	}
}
