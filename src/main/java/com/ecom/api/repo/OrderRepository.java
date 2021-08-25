package com.ecom.api.repo;

import com.ecom.api.entity.Order;
import com.ecom.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByCreator(User creator);
    List<Order> findByStatus(String status);
}
