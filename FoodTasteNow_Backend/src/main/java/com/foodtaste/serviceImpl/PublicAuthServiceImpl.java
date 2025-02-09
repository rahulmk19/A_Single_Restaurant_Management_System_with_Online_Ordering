package com.foodtaste.serviceImpl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foodtaste.dto.UserRequest;
import com.foodtaste.enums.Role;
import com.foodtaste.exception.UserException;
import com.foodtaste.model.User;
import com.foodtaste.repository.UserRepository;
import com.foodtaste.security.jwt.JwtUtility;
import com.foodtaste.service.PublicAuthService;
import com.foodtaste.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PublicAuthServiceImpl implements PublicAuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtUtility jwtUtility;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public String register(UserRequest request) {
		log.info("Creating user with email: {} and mobile: {}", request.getEmail(), request.getMobileNumber());
		if (userRepository.existsByEmail(request.getEmail())) {
			log.warn("Email already exists: {}", request.getEmail());
			throw new UserException("Email already exists");
		}
		if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
			log.warn("Mobile number already exists: {}", request.getMobileNumber());
			throw new UserException("Mobile number already exists");
		}

		User user = new User();
		user.setUuid(UUID.randomUUID().toString());
		user.setEmail(request.getEmail());
		user.setMobileNumber(request.getMobileNumber());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		
		userRepository.save(user);
		return "Registration Successful";
	}

	@Override
	public String login(UserRequest request) {

		User user = userService.findByEmail(request.getEmail())
				.orElseThrow(() -> new UserException("User not found with email: " + request.getEmail()));

		if (user.isDeleted()) {
			throw new UserException("Your account has been deactivated. Please reactivate it before logging in.");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

		if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
			throw new UserException("Unauthorized , password is incorrect ");
		}

		if (!userDetails.isAccountNonExpired()) {
			throw new UserException("Account is expired !");
		}
		if (!userDetails.isCredentialsNonExpired()) {
			throw new UserException("Account credentials are expired !");
		}

		Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(),
				request.getPassword(), userDetails.getAuthorities());

		authenticationManager.authenticate(authentication);
		return jwtUtility.generateToken(userDetails.getUsername());
	}

}
