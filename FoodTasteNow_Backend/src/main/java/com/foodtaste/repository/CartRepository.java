package com.foodtaste.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodtaste.model.Cart;
import com.foodtaste.model.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	Optional<Cart> findByUserAndCheckout(User user, boolean checkout);
}
