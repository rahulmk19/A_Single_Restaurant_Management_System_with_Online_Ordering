package com.foodtaste.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

	private Long id;

	private String username;

	private String name;

	private String email;

	private String mobileNumber;

	private String roles;

	private boolean isActive;
}
