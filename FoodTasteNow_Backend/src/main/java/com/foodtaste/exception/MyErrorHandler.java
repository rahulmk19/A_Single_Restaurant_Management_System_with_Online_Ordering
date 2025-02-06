package com.foodtaste.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyErrorHandler {

	private String error;
	private String message;
	private LocalDateTime timestamp;

}
