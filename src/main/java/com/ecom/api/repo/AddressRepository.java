package com.ecom.api.repo;

import com.ecom.api.entity.Address;
import com.ecom.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
//    @Query("select a from address where a.ownerId = ?1")
    List<Address> findAllByOwner(User owner);
}
