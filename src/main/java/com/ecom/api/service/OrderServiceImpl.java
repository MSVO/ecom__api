package com.ecom.api.service;

import com.ecom.api.entity.Address;
import com.ecom.api.entity.Order;
import com.ecom.api.entity.Product;
import com.ecom.api.entity.User;
import com.ecom.api.repo.AddressRepository;
import com.ecom.api.repo.OrderRepository;
import com.ecom.api.repo.ProductRepository;
import com.ecom.api.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private UserRepository userRepo;

    @Override
    public Order createOrder(Integer creatorId, Integer addressId, Integer productId, Integer quantity) throws Exception {
        User creator = userRepo.findById(creatorId).get();
        Address address = addressRepo.findById(addressId).get();
        Product product = productRepo.findById(productId).get();
        Order newOrder = new Order();
        newOrder.setCreator(creator);
        newOrder.setAddress(address);
        newOrder.setProduct(product);
        newOrder.setQuantity(quantity);
        newOrder = orderRepo.save(newOrder);
        return newOrder;
    }

    @Override
    public Optional<Order> findById(Integer orderId) {
        return orderRepo.findById(orderId);
    }
}
