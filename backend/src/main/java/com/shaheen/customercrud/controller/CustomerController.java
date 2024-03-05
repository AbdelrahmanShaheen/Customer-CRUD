package com.shaheen.customercrud.controller;

import com.shaheen.customercrud.requestEntity.CustomerRegistrationRequest;
import com.shaheen.customercrud.service.CustomerService;
import com.shaheen.customercrud.requestEntity.CustomerUpdateRequest;
import com.shaheen.customercrud.entity.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Customer>> getCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    @GetMapping("{customerId}")
    public ResponseEntity<Customer> getCustomer(
            @PathVariable("customerId") Integer customerId) {
        return new ResponseEntity<>(customerService.getCustomer(customerId) ,HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<String> registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        customerService.addCustomer(customerRegistrationRequest);
        return new ResponseEntity<>("Customer is registered successfully",HttpStatus.CREATED);
    }
    @DeleteMapping("{customerId}")
    public ResponseEntity<?> removeCustomer(@PathVariable("customerId") Integer customerId){
        customerService.removeCustomer(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Integer customerId,
                               @RequestBody CustomerUpdateRequest customerUpdateRequest){
        customerService.updateCustomer(customerId ,customerUpdateRequest);
    }
}
