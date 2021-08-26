package com.ecom.api.controller;

import com.ecom.api.body.CreateOrderRequestBody;
import com.ecom.api.entity.Order;
import com.ecom.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ControllerUtility controllerUtility;

    @GetMapping("/")
    public Object queryOrder(
            @RequestParam(name="status", required = false) String status,
            @RequestHeader(value="Authorization") String token
    ){
        // TODO: Verify admin token
        Map<String, Object> matcher = new HashMap<>();
        System.out.println(status);
        if (status != null) {
            matcher.put("status", status);
        }
        List<Order> queriedOrders = orderService.findAllByMatchers(matcher);
        for (Order order: queriedOrders
             ) {
            order.getCreator().setUserRoles(null);
        }
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("orders", queriedOrders);
        return responseBody;
    }

    @PostMapping("/")
    public Object createOrder(
            @RequestHeader(value="Authorization") String token,
            @RequestBody CreateOrderRequestBody requestBody
            ) throws Exception {
        Integer creatorId = controllerUtility.obtainUserIdFromToken(token);
        Integer addressId = requestBody.getAddressId();
        Integer productId = requestBody.getProductId();
        Integer quantity = requestBody.getQuantity();
        Order createdOrder = orderService.createOrder(creatorId, addressId, productId, quantity, "PLACED");
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("createdOrder", createdOrder);
        return responseBody;
    }

    @GetMapping("/{orderId}")
    public Object getOrderDetails(
            @RequestHeader(value="Authorization") String token,
            @PathVariable Integer orderId
    ) throws Exception {
        // TODO: Authorization
        Order order = orderService.findById(orderId).get();
        order.getCreator().setUserRoles(null);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("order", order);
        return responseBody;
    }

    private Order processedOrder(Order order) {
        order.getCreator().setUserRoles(null);
        return order;
    }

    private Order performAction(Integer orderId, String action, String remark) throws Exception {
        String status = null;
        if (action.equals("ACCEPT")) {
            return orderService.acceptOrderById(orderId, remark);
        }
        if (action.equals("REJECT")) {
            return orderService.rejectOrderById(orderId, remark);
        }
        throw new Exception("Invalid action");
    }

    @PostMapping("/{orderId}")
    public Object processPostRequest(
            @RequestHeader(value="Authorization") String token,
            @PathVariable Integer orderId,
            @RequestBody Map<String, Object> requestBody
    ) throws Exception {
        // TODO: Authorization
        if (requestBody.get("action") != null) {
            Order updatedOrder = performAction(orderId, (String) requestBody.get("action"), (String) requestBody.get("remark"));
            updatedOrder.getCreator().setUserRoles(null);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("updatedOrder", updatedOrder);
            return responseBody;
        } else {
            throw new Exception("No action given");
        }
    }
}
