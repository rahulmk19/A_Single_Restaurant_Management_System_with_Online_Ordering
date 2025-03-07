package com.foodtaste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodtaste.model.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

}
