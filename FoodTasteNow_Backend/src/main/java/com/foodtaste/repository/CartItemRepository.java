package com.foodtaste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodtaste.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
