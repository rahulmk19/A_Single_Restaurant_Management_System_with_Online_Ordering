package com.foodtaste.dto;

import java.util.HashSet;
import java.util.Set;

import com.foodtaste.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

	private Integer id;

	private String username;

	private String name;

	private String email;

	private String mobileNumber;

	Set<Role> roles = new HashSet<>();
}
