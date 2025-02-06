package com.foodtaste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodtaste.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	boolean existsByEmail(String email);

	boolean existsByMobileNumber(String mobileNumber);

}
