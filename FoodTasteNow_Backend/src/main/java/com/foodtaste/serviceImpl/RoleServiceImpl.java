package com.foodtaste.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.foodtaste.model.Role;
import com.foodtaste.repository.RoleRepo;
import com.foodtaste.service.RoleService;

import jakarta.annotation.PostConstruct;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepo roleRepo;

	public RoleServiceImpl(RoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}

	@Override
	public Role createNewRole(Role role) {
		return roleRepo.save(role);
	}

	@Override
	public Role updateRole(Long roleId, Role role) {
		Role existingRole = getRoleById(roleId);
		existingRole.setRoleName(role.getRoleName());
		existingRole.setRoleDesc(role.getRoleDesc());
		return roleRepo.save(existingRole);
	}

	@Override
	public void deleteRole(Long roleId) {
		roleRepo.deleteById(roleId);
	}

	@Override
	public Role getRoleById(Long roleId) {
		return roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
	}

	@Override
	public List<Role> getAllRoles() {
		return roleRepo.findAll();
	}

	/**
	 * This method is executed after the bean initialization to ensure that the
	 * default roles are present in the system.
	 */
	@PostConstruct
	public void initRoles() {
		List<Role> defaultRoles = Arrays.asList(new Role(null, "SUPRIME_ADMIN", "Role for supreme administrators"),
				new Role(null, "ADMIN", "Role for administrators"), new Role(null, "USER", "Role for standard users"),
				new Role(null, "CTO", "Role for Chief Technology Officers"),
				new Role(null, "MANAGER", "Role for managers"));

		for (Role role : defaultRoles) {
			Optional<Role> existing = roleRepo.findByRoleName(role.getRoleName());
			if (!existing.isPresent()) {
				roleRepo.save(role);
			}
		}
	}
}
