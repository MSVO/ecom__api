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

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    public Order createOrder(Integer creatorId, Integer addressId, Integer productId, Integer quantity, String status) throws Exception {
        User creator = userRepo.findById(creatorId).get();
        Address address = addressRepo.findById(addressId).get();
        Product product = productRepo.findById(productId).get();
        Order newOrder = new Order();
        newOrder.setCreator(creator);
        newOrder.setAddress(address);
        newOrder.setProduct(product);
        newOrder.setQuantity(quantity);
        newOrder.setStatus(status);
        newOrder.setCreationDate(new Date());
        newOrder = orderRepo.save(newOrder);
        return newOrder;
    }

    @Override
    public Optional<Order> findById(Integer orderId) {
        return orderRepo.findById(orderId);
    }

    @Override
    public List<Order> findAllByMatchers(Map<String, Object> matchers) {

        // TODO: Extend matcher to use other columns
        if (matchers.containsKey("status")) {
            return orderRepo.findByStatus(matchers.get("status").toString().toUpperCase());
        } else {
            return orderRepo.findAll();
        }

    }

    @Override
    public Order save(Order order) {
        return orderRepo.save(order);
    }

    @Override
    public List<Order> findAllByCreator(User creator) {
        return orderRepo.findAllByCreator(creator);
    }

    @Override
    @Transactional
    public Order acceptOrderById(Integer orderId, String remark) throws Exception {
        Order order = findById(orderId).get();
        if (order.getStatus().equals("PLACED")) {
            Integer newStock = order.getProduct().getStock_quantity() - order.getQuantity();
            if (newStock < 0) {
                throw new Exception("Not enough stock");
            }
            Product product = order.getProduct();
            product.setStock_quantity(newStock);
            productRepo.save(product);
            order.setStatus("ACCEPTED");
            order.setRemark(remark);
            orderRepo.save(order);
            return orderRepo.findById(orderId).get();
        } else {
            throw new Exception("Invalid action");
        }
    }

    @Override
    @Transactional
    public Order rejectOrderById(Integer orderId, String remark) throws Exception {
        Order order = findById(orderId).get();
        if (order.getStatus().equals("PLACED")) {
            order.setStatus("REJECTED");
            order.setRemark(remark);
            orderRepo.save(order);
            return orderRepo.findById(orderId).get();
        } else {
            throw new Exception("Invalid action");
        }
    }
}
