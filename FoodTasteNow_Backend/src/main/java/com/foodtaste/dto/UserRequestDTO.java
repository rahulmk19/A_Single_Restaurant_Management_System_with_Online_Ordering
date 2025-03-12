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
public class UserRequestDTO {

	@NotBlank(message = "Username must not be blank")
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]*$", message = "Username should start with an alphabet and contain only alphanumeric characters")
	private String username;

	@NotBlank(message = "Name must not be blank")
	@Size(min = 3, message = "Name should be greater than 3 characters")
	private String name;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email must not be blank")
	private String email;

	@Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits long")
	private String mobileNumber;

	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
	private String password;
}
