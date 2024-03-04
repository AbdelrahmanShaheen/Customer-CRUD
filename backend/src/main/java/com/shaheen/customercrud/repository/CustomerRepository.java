package com.shaheen.customercrud.repository;

import com.shaheen.customercrud.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    //using jpql to query using the name
    boolean existsCustomerByEmail(String email);
    boolean existsPersonById(Integer id);
}
