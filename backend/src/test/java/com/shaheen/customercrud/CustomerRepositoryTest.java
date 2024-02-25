package com.shaheen.customercrud;

import com.shaheen.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers{
    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void existsCustomerByEmailShouldSuccessIfEmailIsPresent() {
        // given
        String email = "ahmed1@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.save(customer);
        // when
        boolean actual = underTest.existsCustomerByEmail(email);
        // then
        assertThat(actual).isTrue();
    }
    @Test
    void existsCustomerByEmailShouldFailIfEmailIsNotPresent() {
        // given
        String email = "ahmed@gmailcom";
        // when
        boolean actual = underTest.existsCustomerByEmail(email);
        // then
        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonByIdShouldSuccessIfIdIsPresent() {
        // given
        String email = "ahmed1@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.save(customer);
        Integer id = underTest.findAll().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // when
        boolean actual = underTest.existsPersonById(id);
        // then
        assertThat(actual).isTrue();
    }
    @Test
    void existsPersonByIdShouldFailIfIdIsNotPresent() {
        // given
        Integer id = -1;
        // when
        boolean actual = underTest.existsPersonById(id);
        // then
        assertThat(actual).isFalse();
    }
}