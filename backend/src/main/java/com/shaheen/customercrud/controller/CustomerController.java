package com.shaheen.customercrud.controller;

import com.shaheen.customercrud.requestEntity.CustomerRegistrationRequest;
import com.shaheen.customercrud.service.CustomerService;
import com.shaheen.customercrud.requestEntity.CustomerUpdateRequest;
import com.shaheen.customercrud.entity.Customer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomer(
            @PathVariable("customerId") Integer customerId) {
        return customerService.getCustomer(customerId);
    }
    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        customerService.addCustomer(customerRegistrationRequest);
    }
    @DeleteMapping("{customerId}")
    public void removeCustomer(@PathVariable("customerId") Integer customerId){
        customerService.removeCustomer(customerId);
    }
    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Integer customerId,
                               @RequestBody CustomerUpdateRequest customerUpdateRequest){
        customerService.updateCustomer(customerId ,customerUpdateRequest);
    }
}