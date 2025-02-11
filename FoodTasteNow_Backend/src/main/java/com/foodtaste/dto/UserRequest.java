package com.foodtaste.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserRequest {

	@NotBlank
	@Size(min = 3, message = "Name should be greater than 3 characters")
	private String name;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email must not be blank")
	private String email;

	@Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits long")
	private String mobileNumber;
	
	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	private String password;

}
