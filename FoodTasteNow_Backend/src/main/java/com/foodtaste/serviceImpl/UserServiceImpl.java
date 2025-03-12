package com.foodtaste.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

			Set<Role> adminRoles = new HashSet<>();

			Role adminRole = new Role();
			adminRole.setRoleName("Admin");
			adminRole.setRoleDesc("Admin Role");
			roleRepo.save(adminRole);

			adminRoles.add(adminRole);
			adminUser.setRoles(adminRoles);
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

		Set<Role> userRole = new HashSet<>();
		Role role = roleRepo.findByRoleName("User").orElseGet(() -> {
			Role newRole = new Role();
			newRole.setRoleName("User");
			newRole.setRoleDesc("Default user role");
			return roleRepo.save(newRole);
		});
		userRole.add(role);
		newUser.setRoles(userRole);

		User savedUser = userRepo.save(newUser);
		return modelMapper.map(savedUser, UserResponseDTO.class);
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		log.info("Fetching all users from the database");
		List<User> allUsers = userRepo.findAll();
		return allUsers.stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).collect(Collectors.toList());
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
		user.setDeleted(true);
		userRepo.save(user);
		log.info("User soft deleted successfully with ID: {}", userId);
		return "User soft deleted successfully with ID " + userId;
	}

}
