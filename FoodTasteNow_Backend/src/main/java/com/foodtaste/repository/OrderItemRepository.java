package com.foodtaste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodtaste.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
