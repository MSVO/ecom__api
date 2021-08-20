package com.ecom.api.controller;

import com.ecom.api.body.UserTokenRequestBody;
import com.ecom.api.entity.User;
import com.ecom.api.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    private String makeToken(User user) {
        return user.getId().toString();
    }

    @PostMapping("/")
    public Object requestUserToken(@RequestBody UserTokenRequestBody requestBody) throws Exception {
        User user = userRepo.findByEmail(requestBody.getEmail()).get();
        if (user.getPassword().equals(requestBody.getPassword())) {
            String token = makeToken(user);
            Map<String, Object> body = new HashMap<>();
            body.put("token", token);
            return body;
        } else {
            throw new Exception("Password mismatch");
        }
    }
}
