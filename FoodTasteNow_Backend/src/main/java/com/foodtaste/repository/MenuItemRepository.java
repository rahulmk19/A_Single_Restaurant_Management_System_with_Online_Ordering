package com.foodtaste.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foodtaste.model.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

	@Query("SELECT m FROM MenuItem m WHERE LOWER(m.name) = LOWER(:name)")
	Optional<MenuItem> findByNameIgnoreCase(String name);

}
