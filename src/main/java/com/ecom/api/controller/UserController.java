package com.ecom.api.controller;

import com.ecom.api.entity.Address;
import com.ecom.api.entity.User;
import com.ecom.api.repo.AddressRepository;
import com.ecom.api.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private ControllerUtility controllerUtility;

    private Map<String, Object> processAddress(Address address) {
        Map<String, Object> processedAddress = new HashMap<>();
        processedAddress.put("id", address.getId());
        processedAddress.put("name", address.getName());
        processedAddress.put("fullAddress", address.getFullAddress());
        processedAddress.put("pinCode", address.getPinCode());
        processedAddress.put("email", address.getEmail());
        processedAddress.put("mobile", address.getMobile());
        return processedAddress;
    }

    @GetMapping("/addresses")
    private Object fetchAddressesOfUser(
            @RequestHeader(value="Authorization") String token) throws Exception {
        User authenticatedUser = controllerUtility.obtainUserFromToken(token);
        List<Address> addressList = addressRepo.findAllByOwner(authenticatedUser);
        List<Object> processedAddressList = new ArrayList<>();
        for (Address address :
                addressList) {
            processedAddressList.add(processAddress(address));
        }
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("addresses", processedAddressList);
        return responseBody;
    }
}
