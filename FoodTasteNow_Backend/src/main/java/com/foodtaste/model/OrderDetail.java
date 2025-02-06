package com.foodtaste.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodtaste.enums.StatusEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "OrderDetail")
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank
	@Size(min = 4, max = 30, message = "Customer name should be greater than 4")
	private String customerName;

	@NotBlank
	@Size(min = 10, max = 10, message = "Phone number should be 10 digit number")
	private String customerPhone;

	private BigDecimal totalAmount;

	private Integer quantity;

	@Enumerated(EnumType.STRING)
	private StatusEnum status;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JsonManagedReference
	private List<OrderItem> items;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user; // This creates a unidirectional relationship

}
