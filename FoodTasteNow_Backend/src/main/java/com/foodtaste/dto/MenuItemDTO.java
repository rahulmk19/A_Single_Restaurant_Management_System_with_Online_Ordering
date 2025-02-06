package com.foodtaste.dto;

import java.math.BigDecimal;

import com.foodtaste.enums.CategoryEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDTO {

	private Integer id;

	@NotBlank
	private String name;

	@NotBlank
	@Min(value = 1)
	private BigDecimal price;

	@NotBlank
	@Min(value = 0)
	private Integer quantity;

	@NotBlank
	@Enumerated(EnumType.STRING)
	private CategoryEnum category;

}
