package com.ecom.api.controller;

import com.ecom.api.body.CreateOrderRequestBody;
import com.ecom.api.entity.Order;
import com.ecom.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ControllerUtility controllerUtility;

    @PostMapping("/")
    public Object createOrder(
            @RequestHeader(value="Authorization") String token,
            @RequestBody CreateOrderRequestBody requestBody
            ) throws Exception {
        Integer creatorId = controllerUtility.obtainUserIdFromToken(token);
        Integer addressId = requestBody.getAddressId();
        Integer productId = requestBody.getProductId();
        Integer quantity = requestBody.getQuantity();
        Order createdOrder = orderService.createOrder(creatorId, addressId, productId, quantity);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("createdOrder", createdOrder);
        return responseBody;
    }

    @GetMapping("/{orderId}")
    public Object getOrderDetails(
            @RequestHeader(value="Authorization") String token,
            @PathVariable Integer orderId
    ) throws Exception {
        Order order = orderService.findById(orderId).get();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("order", order);
        return responseBody;
    }
}
