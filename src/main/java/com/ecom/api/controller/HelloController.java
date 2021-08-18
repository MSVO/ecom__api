package com.ecom.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/")
    public Map<String, String> sayHello() {
        Map<String, String> body = new HashMap<>();
        body.put("msg", "Hello World!");
        return body;
    }
}
