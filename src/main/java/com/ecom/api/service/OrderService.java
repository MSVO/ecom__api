package com.ecom.api.service;

import com.ecom.api.entity.Order;
import com.ecom.api.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderService {
    Order createOrder(Integer creatorId, Integer addressId, Integer productId, Integer quantity) throws Exception;
    Optional<Order> findById(Integer orderId);
    List<Order> findAllByCreator(User creator);
    List<Order> findAllByMatchers(Map<String, Object> matchers);
    Order save(Order order);
}
