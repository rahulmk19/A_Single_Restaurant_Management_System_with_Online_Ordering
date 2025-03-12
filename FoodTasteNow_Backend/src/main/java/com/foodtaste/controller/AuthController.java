package com.foodtaste.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodtaste.constant.AppConstants;
import com.foodtaste.dto.LoginRequestDTO;
import com.foodtaste.dto.LoginResponseDTO;
import com.foodtaste.dto.UserRequestDTO;
import com.foodtaste.service.AuthService;
import com.foodtaste.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.APP_NAME + AppConstants.PUBLIC_ROUTE_TYPE + AppConstants.Auth)
@CrossOrigin(origins = "*")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
		if (userRequestDTO == null || !userRequestDTO.getUsername().matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
			throw new RuntimeException("Username should have only alphabets or alphabets with number.");
		}
		try {
			logger.info("Request to save user: {}", userRequestDTO);
			userService.createNewUser(userRequestDTO);
			return new ResponseEntity<String>("User registered successfully", HttpStatus.CREATED);
		} catch (Exception ex) {
			logger.error("Failed to save user: {}", ex.getMessage(), ex);
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDto) {
		logger.info("Login request received, verifying user: {}", loginRequestDto.getUsername());
		try {
			LoginResponseDTO response = authService.verifyLogin(loginRequestDto);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (BadCredentialsException e) {
			logger.error("Bad credentials for user: {}", loginRequestDto.getUsername(), e);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			logger.error("Error during login for user: {}", loginRequestDto.getUsername(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
