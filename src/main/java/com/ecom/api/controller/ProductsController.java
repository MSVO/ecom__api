package com.ecom.api.controller;

import com.ecom.api.entity.Product;
import com.ecom.api.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/")
    public Object listProductsResponse() {
        List<Product> products = productRepo.findAll();
        Map<String, Object> body = new HashMap<>();
        body.put("products", products);
        return body;
    }

    @GetMapping("/{productId}")
    public Object getProductResponse(@PathVariable Integer productId) throws Exception {
        Product product = productRepo.findById(productId).get();
        Map<String, Object> body = new HashMap<>();
        body.put("product", product);
        return body;
    }
}
