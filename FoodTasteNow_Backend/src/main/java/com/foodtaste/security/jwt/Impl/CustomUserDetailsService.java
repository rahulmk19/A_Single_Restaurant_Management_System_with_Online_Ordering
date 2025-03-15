package com.foodtaste.security.jwt.Impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.foodtaste.model.User;
import com.foodtaste.repository.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String loginAttribute) throws UsernameNotFoundException {
		if (loginAttribute == null || loginAttribute.trim().isEmpty()) {
			throw new RuntimeException("Login attribute cannot be null or empty");
		}

		User user;
		if (loginAttribute.matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
			user = userRepo.findByUsername(loginAttribute).orElseThrow(
					() -> new UsernameNotFoundException("User not found with username: " + loginAttribute));
		} else if (loginAttribute.matches("\\d{10}")) {
			user = userRepo.findByMobileNumber(loginAttribute).orElseThrow(
					() -> new UsernameNotFoundException("User not found with mobile number: " + loginAttribute));
		} else if (loginAttribute.contains("@")) {
			user = userRepo.findByEmail(loginAttribute)
					.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginAttribute));
		} else {
			throw new UsernameNotFoundException("Invalid username format");
		}

		GrantedAuthority authorities = new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName());

		return new CustomUserDetails(loginAttribute, user.getPassword(), Collections.singleton(authorities));
	}

}
