package com.shaheen.customercrud.dao;

import com.shaheen.customercrud.repository.CustomerRepository;
import com.shaheen.customercrud.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao {
    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        return customerRepository.existsPersonById(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
