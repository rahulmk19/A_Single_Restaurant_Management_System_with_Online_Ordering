package com.foodtaste.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodtaste.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	boolean existsByEmail(String email);

	boolean existsByMobileNumber(String mobileNumber);

	Optional<User> findByEmail(String email);

}
