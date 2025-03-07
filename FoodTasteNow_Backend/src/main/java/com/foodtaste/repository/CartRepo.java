package com.foodtaste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodtaste.model.Cart;
import com.foodtaste.model.User;

public interface CartRepo extends JpaRepository<Cart, Integer> {

	Cart findByUser(User user);
}
