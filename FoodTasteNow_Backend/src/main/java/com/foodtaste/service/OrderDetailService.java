package com.foodtaste.service;

import java.util.List;

import com.foodtaste.dto.OrderRequest;
import com.foodtaste.dto.OrderResponse;
import com.foodtaste.enums.StatusEnum;
import com.foodtaste.model.OrderDetail;

public interface OrderDetailService {

	OrderResponse createOrder(Integer userId, OrderRequest request);

	OrderDetail getOrderById(Integer id);

	OrderDetail getOrderByIdWithItems(Integer id);

	List<OrderDetail> getAllOrders();

	OrderDetail updateOrderStatus(Integer id, StatusEnum status);

	OrderDetail cancelOrderById(Integer id);

}
