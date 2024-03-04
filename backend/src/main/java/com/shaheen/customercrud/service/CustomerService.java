package com.shaheen.customercrud.service;

import com.shaheen.customercrud.requestEntity.CustomerRegistrationRequest;
import com.shaheen.customercrud.requestEntity.CustomerUpdateRequest;
import com.shaheen.customercrud.dao.CustomerDao;
import com.shaheen.customercrud.entity.Customer;
import com.shaheen.customercrud.exception.DuplicateResourceException;
import com.shaheen.customercrud.exception.RequestValidationException;
import com.shaheen.customercrud.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound(
                        "customer with id [%s] not found".formatted(id)
                ));
    }
    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        // check unique email
        String email = customerRegistrationRequest.email();
        if(customerDao.existsPersonWithEmail(email)){
            throw new DuplicateResourceException("email already taken");
        }
        //add customer
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setAge(customerRegistrationRequest.age());
        customer.setName(customerRegistrationRequest.name());

        customerDao.insertCustomer(customer);
    }
    public void removeCustomer(Integer id){
        if(!customerDao.existsPersonWithId(id)){
            throw new ResourceNotFound(
                    "customer with id [%s] not found".formatted(id)
            );
        }
        customerDao.deleteCustomerById(id);
    }
    public void updateCustomer(Integer id, CustomerUpdateRequest customerUpdateRequest){
        Customer customer = getCustomer(id);
        String name = customerUpdateRequest.name();
        String email = customerUpdateRequest.email();
        Integer age = customerUpdateRequest.age();

        boolean changes = false;
        if(name != null && !customer.getName().equals(name)){
            customer.setName(name);
            changes = true;
        }
        if(age != null && !customer.getAge().equals(age)){
            customer.setAge(age);
            changes = true;
        }
        if(email != null && !customer.getEmail().equals(email)){
            if(customerDao.existsPersonWithEmail(email)){
                throw new DuplicateResourceException("email already taken");
            }
            customer.setEmail(email);
            changes = true;
        }
        if(!changes){
            throw new RequestValidationException("no data changes found");
        }
        customerDao.updateCustomer(customer);
    }
}
