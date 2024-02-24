package com.shaheen.customercrud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


class CustomerJPADataAccessServiceTest {
    private CustomerJPADataAccessService underTest;
    private CustomerRepository customerRepository;
    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @Test
    void selectAllCustomersShouldSuccess() {
        // when
        underTest.selectAllCustomers();
        // then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerByIdShouldSuccess() {
        // given
        Integer id = 5;
        // when
        underTest.selectCustomerById(id);
        // then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomerShouldSuccess() {
        // given
        String email = "ahmed1@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        // when
        underTest.insertCustomer(customer);
        // then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmailShouldSuccess() {
        // given
        String email = "ahmed1@gmail.com";
        // when
        underTest.existsPersonWithEmail(email);
        // then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomerByIdShouldSuccess() {
        // given
        Integer id = 5;
        // when
        underTest.deleteCustomerById(id);
        // then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void existsPersonWithIdShouldSuccess() {
        // given
        Integer id = 5;
        // when
        underTest.existsPersonWithId(id);
        // then
        verify(customerRepository).existsPersonById(id);
    }

    @Test
    void updateCustomerShouldSuccess() {
        // given
        String email = "ahmed1@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        // when
        underTest.updateCustomer(customer);
        // then
        verify(customerRepository).save(customer);
    }
}