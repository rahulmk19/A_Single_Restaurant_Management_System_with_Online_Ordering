package com.foodtaste.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foodtaste.dto.Profile;
import com.foodtaste.dto.UserRequestDTO;
import com.foodtaste.dto.UserResponseDTO;
import com.foodtaste.exception.UserException;
import com.foodtaste.model.Role;
import com.foodtaste.model.User;
import com.foodtaste.repository.RoleRepo;
import com.foodtaste.repository.UserRepo;
import com.foodtaste.service.UserService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void initRoleAndUsers() {

		Optional<User> admin = userRepo.findByUsername("admin123");
		if (!admin.isPresent()) {
			User adminUser = new User();
			adminUser.setName("Admin");
			adminUser.setUsername("admin123");
			adminUser.setEmail("admin@gmail.com");
			adminUser.setPassword(passwordEncoder.encode("Admin@123"));
			adminUser.setMobileNumber("7667869850");

			Role adminRole = roleRepo.findByRoleName("Admin")
					.orElseThrow(() -> new RuntimeException("AdminRole not found"));

			adminUser.setRole(adminRole);
			userRepo.save(adminUser);
		}

	}

	@Override
	public UserResponseDTO createNewUser(UserRequestDTO userRequestDTO) {
		log.info("Creating user with email: {} and mobile: {}", userRequestDTO.getEmail(),
				userRequestDTO.getMobileNumber());

		Optional<User> isEmailExits = userRepo.findByEmail(userRequestDTO.getEmail());
		if (isEmailExits.isPresent()) {
			log.warn("Email already exists: {}", userRequestDTO.getEmail());
			throw new UserException("Email already exists");
		}

		Optional<User> isUsernameExits = userRepo.findByUsername(userRequestDTO.getUsername());
		if (isUsernameExits.isPresent()) {
			log.warn("Username already exists: {}", userRequestDTO.getUsername());
			throw new UserException("Username already exists");
		}

		if (userRepo.existsByMobileNumber(userRequestDTO.getMobileNumber())) {
			log.warn("Mobile number already exists: {}", userRequestDTO.getMobileNumber());
			throw new UserException("Mobile number already exists");
		}

		User newUser = new User();
		newUser.setUsername(userRequestDTO.getUsername());
		newUser.setName(userRequestDTO.getName());
		newUser.setEmail(userRequestDTO.getEmail());
		newUser.setMobileNumber(userRequestDTO.getMobileNumber());
		newUser.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

		Role role = roleRepo.findByRoleName("User")
				.orElseThrow(() -> new RuntimeException("RoleName not found" + "User"));
		newUser.setRole(role);

		User savedUser = userRepo.save(newUser);
		System.out.print(savedUser);
		return modelMapper.map(savedUser, UserResponseDTO.class);
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		log.info("Fetching all users from the database");
		List<User> allUsers = userRepo.findAll();

		List<UserResponseDTO> userResponseDTO = allUsers.stream()
				.map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getName(), user.getEmail(),
						user.getMobileNumber(), user.getRole().getRoleName(), user.isActive()))
				.collect(Collectors.toList());
		return userResponseDTO;
	}

	@Override
	public UserResponseDTO getUserById(Long userId) {
		log.info("Fetching user with ID: {}", userId);
		User user = userRepo.findById(userId).orElseThrow(() -> {
			log.warn("User not found with ID: {}", userId);
			return new UserException("User not found with ID: " + userId);
		});
		return modelMapper.map(user, UserResponseDTO.class);
	}

	@Override
	public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
		log.info("Updating user with ID: {}", userId);

		User user = userRepo.findById(userId).orElseThrow(() -> {
			log.warn("User not found with ID: {}", userId);
			return new UserException("User not found with ID: " + userId);
		});

		if (!user.getEmail().equals(userRequestDTO.getEmail()) && userRepo.existsByEmail(userRequestDTO.getEmail())) {
			log.warn("Email conflict for user update: {}", userRequestDTO.getEmail());
			throw new UserException("Email already exists");
		}

		if (!user.getMobileNumber().equals(userRequestDTO.getMobileNumber())
				&& userRepo.existsByMobileNumber(userRequestDTO.getMobileNumber())) {
			log.warn("Mobile number conflict for user update: {}", userRequestDTO.getMobileNumber());
			throw new UserException("Mobile number already exists");
		}

		if (!user.getUsername().equals(userRequestDTO.getUsername())
				&& userRepo.existsByUsername(userRequestDTO.getUsername())) {
			log.warn("Username conflict for user update: {}", userRequestDTO.getUsername());
			throw new UserException("Username already exists");
		}

		user.setUsername(userRequestDTO.getUsername());
		user.setName(userRequestDTO.getName());
		user.setEmail(userRequestDTO.getEmail());
		user.setMobileNumber(userRequestDTO.getMobileNumber());

		if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
		}

		User savedUser = userRepo.save(user);
		log.info("User updated successfully with ID: {}", savedUser.getUsername());

		return modelMapper.map(savedUser, UserResponseDTO.class);
	}

	@Override
	public String deleteUser(Long userId) {
		log.info("Soft Deleting user with ID: {}", userId);
		User user = userRepo.findById(userId).orElseThrow(() -> {
			log.warn("User not found with ID: {}", userId);
			return new UserException("User not found with ID: " + userId);
		});
		user.setActive(false);
		userRepo.save(user);
		log.info("User soft deleted successfully with ID: {}", userId);
		return "User soft deleted successfully with ID " + userId;
	}

	@Override
	public UserResponseDTO convertUserToAdmin(Long userId, String roleName) {
		User user = userRepo.findById(userId).orElseThrow(() -> new UserException("User not found with ID: " + userId));

		Role role = roleRepo.findByRoleName(roleName)
				.orElseThrow(() -> new UserException("Role not found: " + roleName));

		user.setRole(role);
		User savedUser = userRepo.save(user);

		return modelMapper.map(savedUser, UserResponseDTO.class);
	}

	@Override
	public Profile getUserProfile() {
		User user = getCurrentUser();
		Profile profile = new Profile(user.getName(), user.getUsername(), user.getEmail(), user.getMobileNumber());
		return profile;
	}

	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			throw new UserException("User is not authenticated");
		}
		String username = auth.getName();
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new UserException("User not found with username: " + username));
	}

}
