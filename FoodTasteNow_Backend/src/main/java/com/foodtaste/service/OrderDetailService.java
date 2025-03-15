package com.foodtaste.service;

import java.util.List;

import com.foodtaste.dto.CheckoutOrderRequest;
import com.foodtaste.dto.OrderRequest;
import com.foodtaste.dto.OrderResponse;
import com.foodtaste.enums.StatusEnum;
import com.foodtaste.model.OrderDetail;

public interface OrderDetailService {

	OrderResponse createOrder(OrderRequest request);

	OrderResponse getOrderById(Integer orderId);

	List<OrderResponse> getOrderByUserId(Long userId);

	OrderDetail getOrderByIdWithItems(Integer orderId);

	OrderResponse checkout(CheckoutOrderRequest orderRequest);

	List<OrderResponse> getAllOrders();

	OrderResponse updateOrderStatus(Integer orderId, StatusEnum status);

	OrderResponse cancelOrderById(Integer orderId);

	List<OrderResponse> getOrdersByUser();

}
