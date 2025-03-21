package com.foodtaste.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodtaste.model.Cart;
import com.foodtaste.model.User;

public interface CartRepo extends JpaRepository<Cart, Integer> {

	Optional<Cart> findByUser(User user);
}
