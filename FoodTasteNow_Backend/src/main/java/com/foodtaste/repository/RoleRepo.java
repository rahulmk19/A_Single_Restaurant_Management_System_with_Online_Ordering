package com.foodtaste.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodtaste.model.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

	Optional<Role> findByRoleName(String roleName);
}
