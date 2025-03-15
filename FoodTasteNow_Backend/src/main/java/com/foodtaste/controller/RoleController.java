package com.foodtaste.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodtaste.constant.AppConstants;
import com.foodtaste.model.Role;
import com.foodtaste.service.RoleService;

@RestController
@RequestMapping(AppConstants.APP_NAME + "/roles" + AppConstants.ADMIN)
@CrossOrigin(origins = "*")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping
	public ResponseEntity<Role> createRole(@RequestBody Role role) {
		Role created = roleService.createNewRole(role);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@GetMapping("/{roleId}")
	public ResponseEntity<Role> getRoleById(@PathVariable Long roleId) {
		Role role = roleService.getRoleById(roleId);
		return new ResponseEntity<>(role, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<Role>> getAllRoles() {
		List<Role> roles = roleService.getAllRoles();
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}

	@PutMapping("/{roleId}")
	public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestBody Role role) {
		Role updated = roleService.updateRole(roleId, role);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}

	@DeleteMapping("/{roleId}")
	public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
		roleService.deleteRole(roleId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
