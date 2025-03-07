package com.foodtaste.service;

import java.util.List;

import com.foodtaste.dto.OrderRequest;
import com.foodtaste.dto.OrderResponse;
import com.foodtaste.enums.StatusEnum;
import com.foodtaste.model.OrderDetail;

public interface OrderDetailService {

	OrderResponse createOrder(OrderRequest request);

	OrderResponse getOrderById(Integer orderId);

	OrderDetail getOrderByIdWithItems(Integer orderId);

	List<OrderDetail> getAllOrders();

	OrderDetail updateOrderStatus(Integer orderId, StatusEnum status);

	OrderDetail cancelOrderById(Integer orderId);

}
