package com.ecom.api.controller;

import com.ecom.api.body.NewProductRequestBody;
import com.ecom.api.entity.Product;
import com.ecom.api.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/")
    public Object createProduct(@RequestBody NewProductRequestBody requestBody){
        Product newProduct = new Product();
        newProduct.setName(requestBody.getName());
        newProduct.setPrice(requestBody.getPrice());
        newProduct.setStock_quantity(requestBody.getStockQuantity());
        newProduct.setDescription(requestBody.getDescription());
        newProduct.setImageUrl(requestBody.getImageUrl());
        newProduct = productRepo.save(newProduct);
        Map<String, Object> body = new HashMap<>();
        body.put("createdProduct", newProduct);
        return body;
    }

    @GetMapping("/{productId}")
    public Object getProductResponse(@PathVariable Integer productId) throws Exception {
        Product product = productRepo.findById(productId).get();
        Map<String, Object> body = new HashMap<>();
        body.put("product", product);
        return body;
    }

    @PutMapping("/{productId}")
    public Object putProduct(
            @PathVariable Integer productId,
            @RequestBody NewProductRequestBody requestBody) {
        Product product = productRepo.findById(productId).get();
        product.setName(requestBody.getName());
        product.setDescription(requestBody.getDescription());
        product.setPrice(requestBody.getPrice());
        product.setStock_quantity(requestBody.getStockQuantity());
        product.setImageUrl(requestBody.getImageUrl());
        product = productRepo.save(product);
        Map<String, Object> body = new HashMap<>();
        body.put("updatedProduct", product);
        return body;
    }
}
