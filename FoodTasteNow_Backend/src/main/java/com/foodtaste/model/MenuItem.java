package com.foodtaste.model;

import java.math.BigDecimal;

import com.foodtaste.enums.CategoryEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MenuItem")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Menu Item name cannot be blank")
    @Size(min = 4, max = 30, message = "Menu Item name should be between 4 and 30 characters")
    private String name;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than or equal to 0.01")
    private BigDecimal price;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity should be greater than or equal to 0")
    private Integer quantity;

    @NotNull(message = "Category cannot be null")
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    @NotBlank(message = "Image URL cannot be blank")
    private String imageUrl;
}
