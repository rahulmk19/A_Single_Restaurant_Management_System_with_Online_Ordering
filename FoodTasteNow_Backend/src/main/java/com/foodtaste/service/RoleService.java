package com.foodtaste.service;

import java.util.List;

import com.foodtaste.model.Role;

public interface RoleService {

	Role createNewRole(Role role);

	Role updateRole(Long roleId, Role role);

	void deleteRole(Long roleId);

	Role getRoleById(Long roleId);

	List<Role> getAllRoles();
}
