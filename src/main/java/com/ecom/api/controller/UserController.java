package com.ecom.api.controller;

import com.ecom.api.body.AddAddressRequestBody;
import com.ecom.api.body.UserTokenRequestBody;
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

    @PostMapping("/")
    private  Object createAccountAndGetToken(@RequestBody UserTokenRequestBody requestBody) throws Exception {
        try {
            User existingUser = userRepo.findByEmail(requestBody.getEmail()).get();
            throw new Exception("User already exists");
        } catch (NoSuchElementException e) {
        }
        User newUser = new User();
        newUser.setEmail(requestBody.getEmail());
        newUser.setPassword(requestBody.getPassword());
        newUser = userRepo.save(newUser);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", newUser.getId().toString());
        return responseBody;
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

    @GetMapping("/addresses/{addressId}")
    private Object fetchAddressById(
            @RequestHeader(value="Authorization") String token,
            @PathVariable Integer addressId
    ) throws Exception {
        User authenticatedUser = controllerUtility.obtainUserFromToken(token);
        Address address = addressRepo.findById(addressId).get();
        if (!address.getOwner().getId().equals(authenticatedUser.getId())) {
            throw new Exception("Unauthorized");
        }
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("address",processAddress(address));
        return responseBody;
    }

    @DeleteMapping("/addresses/{addressId}")
    private Object deleteAddressById(
        @RequestHeader(value="Authorization") String token,
        @PathVariable Integer addressId
    ) throws Exception {
        User authenticatedUser = controllerUtility.obtainUserFromToken(token);
        Address address = addressRepo.findById(addressId).get();
        if (!address.getOwner().getId().equals(authenticatedUser.getId())) {
            throw new Exception("Unauthorized");
        }
        addressRepo.deleteById(addressId);
        return new HashMap<>();
    }

    private Address createAddressEntity(User owner, AddAddressRequestBody requestBody) {
        Address newAddress = new Address();
        newAddress.setOwner(owner);
        newAddress.setName(requestBody.getName());
        newAddress.setFullAddress(requestBody.getFullAddress());
        newAddress.setPinCode(requestBody.getPinCode());
        newAddress.setEmail(requestBody.getEmail());
        newAddress.setMobile(requestBody.getMobile());
        return newAddress;
    }

    @PostMapping("/addresses")
    private Object addNewAddress(
            @RequestHeader(value="Authorization") String token,
            @RequestBody AddAddressRequestBody requestBody
    ) throws Exception {
        User authenticatedUser = controllerUtility.obtainUserFromToken(token);
        Address newAddress = createAddressEntity(authenticatedUser, requestBody);
        newAddress = addressRepo.save(newAddress);
        Map<String , Object> responseBody = new HashMap<>();
        responseBody.put("addedAddressId", newAddress.getId());
        return responseBody;
    }
}
