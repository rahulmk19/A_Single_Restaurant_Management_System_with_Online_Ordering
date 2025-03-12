package com.foodtaste.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodtaste.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	Optional<User> findByMobileNumber(String mobileNumber);

	boolean existsByMobileNumber(String mobileNumber);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

}
