package com.foodtaste.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.foodtaste.dto.LoginRequestDTO;
import com.foodtaste.dto.LoginResponseDTO;
import com.foodtaste.model.User;
import com.foodtaste.repository.UserRepo;
import com.foodtaste.security.jwt.Impl.JwtServiceImpl;
import com.foodtaste.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	private final JwtServiceImpl jwtServiceImpl;
	private final AuthenticationManager authManager;
	private final UserRepo userRepo;

	public AuthServiceImpl(JwtServiceImpl jwtServiceImpl, AuthenticationManager authManager, UserRepo userRepo) {
		this.jwtServiceImpl = jwtServiceImpl;
		this.authManager = authManager;
		this.userRepo = userRepo;
	}

	@Override
	public LoginResponseDTO verifyLogin(LoginRequestDTO loginRequestDto) {
		logger.info("Verifying user: {}", loginRequestDto.getUsername());

		try {
			Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
					loginRequestDto.getUsername(), loginRequestDto.getPassword()));

			if (!authentication.isAuthenticated()) {
				logger.error("Authentication failed for user: {}", loginRequestDto.getUsername());
				throw new BadCredentialsException("Bad credentials.");
			}

			SecurityContextHolder.getContext().setAuthentication(authentication);
			logger.info("User authenticated successfully: {}", loginRequestDto.getUsername());

		} catch (BadCredentialsException ex) {
			logger.error("Bad credentials for user: {}", loginRequestDto.getUsername(), ex);
			throw ex;
		} catch (Exception ex) {
			logger.error("Unexpected login error fro user: {}", loginRequestDto.getUsername(), ex);
		}

		String jwtToken = jwtServiceImpl.generateToken(loginRequestDto.getUsername());

		User user = userRepo.findByUsername(loginRequestDto.getUsername()).get();
		LoginResponseDTO response = new LoginResponseDTO();
		response.setUsername(loginRequestDto.getUsername());
		response.setFirstName(user.getName());
		response.setToken(jwtToken);
		return response;
	}

}
