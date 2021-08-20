package com.ecom.api.service;

import com.ecom.api.entity.Order;

import java.util.Optional;

public interface OrderService {
    Order createOrder(Integer creatorId, Integer addressId, Integer productId, Integer quantity) throws Exception;
    Optional<Order> findById(Integer orderId);
}
