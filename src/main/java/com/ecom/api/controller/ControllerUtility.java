package com.ecom.api.controller;

import com.ecom.api.entity.User;
import com.ecom.api.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControllerUtility {

    @Autowired
    private UserRepository userRepo;

    public Integer obtainUserIdFromToken(String token) {
        return Integer.valueOf(token.split(":")[0]);
    }
    public User obtainUserFromToken(String token) throws Exception {
        return userRepo.findById(obtainUserIdFromToken(token)).get();
    }
}
