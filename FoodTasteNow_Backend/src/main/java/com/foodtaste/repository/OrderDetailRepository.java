package com.foodtaste.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foodtaste.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

	@Query("SELECT od FROM OrderDetail od JOIN FETCH od.items WHERE od.id = :id")
	Optional<OrderDetail> findByIdWithItems(@Param("id") Integer id);

}
