package com.shaheen.customercrud;

import com.shaheen.AbstractTestContainers;
import com.shaheen.customercrud.dao.CustomerJDBCDataAccessService;
import com.shaheen.customercrud.dao.CustomerRowMapper;
import com.shaheen.customercrud.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {
    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void dbShouldGetAllCustomers() {
        // given
        Customer customer = new Customer("Ahmed" ,"ahmed@gmail.com" ,22);
        underTest.insertCustomer(customer);
        // when
        List<Customer> actual = underTest.selectAllCustomers();
        // then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void customerShouldExistIfValidId() {
        // given
        String email = "ahmed1@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // when
        Optional<Customer> actual = underTest.selectCustomerById(id);
        // then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }
    @Test
    void customerShouldNotExistIfNotValidId() {
        // given
        Integer id = -1;
        // when
        Optional<Customer> actual = underTest.selectCustomerById(id);
        // then
        assertThat(actual).isEmpty();
    }
    @Test
    void customerShouldBeInserted() {
        // given
        String email = "ahmed12@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // when
        Optional<Customer> actual = underTest.selectCustomerById(id);
        // then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void customerWithValidEmailShouldExist() {
        // given
        String email = "ahmed123@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.insertCustomer(customer);
        // when
        boolean actual = underTest.existsPersonWithEmail(email);
        // then
        assertThat(actual).isTrue();
    }

    @Test
    void customerWithInValidEmailShouldNotExist() {
        // given
        String email = "a@gmail.com";
        // when
        boolean actual = underTest.existsPersonWithEmail(email);
        // then
        assertThat(actual).isFalse();
    }

    @Test
    void customerWithValidIdShouldBeDeleted() {
        // given
        String email = "ahmed1234@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // when
        underTest.deleteCustomerById(id);
        boolean actual = underTest.existsPersonWithEmail(email);
        // then
        assertThat(actual).isFalse();
    }
    @Test
    void customerWithInvalidIdShouldNotBeDeleted() {
        // given
        String email = "ahmed12345@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.insertCustomer(customer);
        Integer invalidId = -1;
        // when
        underTest.deleteCustomerById(invalidId);
        // then
        assertThat(underTest.selectAllCustomers()).isNotEmpty();
    }

    @Test
    void existsPersonWithIdShouldReturnTrueWhenIdPresent() {
        // given
        String email = "ahmed123456@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // when
        boolean actual = underTest.existsPersonWithId(id);
        // then
        assertThat(actual).isTrue();
    }
    @Test
    void existsPersonWithIdShouldReturnFalseWhenIdNotPresent() {
        Integer id = -1;
        // when
        boolean actual = underTest.existsPersonWithId(id);
        // then
        assertThat(actual).isFalse();
    }
    @Test
    void allCustomerFieldsShouldBeUpdatedWhenValidUpdates() {
        // given
        String email = "ahmed1222@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer updatedCustomer = new Customer(id ,"Mohamed" ,"mo225@gmail.com" ,50);
        // when
        underTest.updateCustomer(updatedCustomer);
        Customer retrievedCustomer = underTest.selectCustomerById(id).orElseThrow();
        // then
        assertThat(retrievedCustomer.getName()).isEqualTo("Mohamed");
        assertThat(retrievedCustomer.getEmail()).isEqualTo("mo225@gmail.com");
        assertThat(retrievedCustomer.getAge()).isEqualTo(50);
    }
    @Test
    void customerAgeShouldBeUpdated() {
        // given
        String email = "ahmed12282@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer updatedCustomer = new Customer();
        Integer newAge = 50;
        updatedCustomer.setAge(newAge);
        updatedCustomer.setId(id);
        // when
        underTest.updateCustomer(updatedCustomer);
        Customer retrievedCustomer = underTest.selectCustomerById(id).orElseThrow();
        // then
        assertThat(retrievedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(retrievedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(retrievedCustomer.getAge()).isEqualTo(newAge);
    }
    @Test
    void customerEmailShouldBeUpdated() {
        // given
        String email = "ahmed121922@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer updatedCustomer = new Customer();
        String newEmail = "shaheenabdelrahman28@gmail.com";
        updatedCustomer.setEmail(newEmail);
        updatedCustomer.setId(id);
        // when
        underTest.updateCustomer(updatedCustomer);
        Customer retrievedCustomer = underTest.selectCustomerById(id).orElseThrow();
        // then
        assertThat(retrievedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(retrievedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(retrievedCustomer.getAge()).isEqualTo(customer.getAge());
    }
    @Test
    void customerNameShouldBeUpdated() {
        // given
        String email = "ahmed122772@gmail.com";
        Customer customer = new Customer("Ahmed" ,email ,22);
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer updatedCustomer = new Customer();
        String newName = "Abdelrahman";
        updatedCustomer.setName(newName);
        updatedCustomer.setId(id);
        // when
        underTest.updateCustomer(updatedCustomer);
        Customer retrievedCustomer = underTest.selectCustomerById(id).orElseThrow();
        // then
        assertThat(retrievedCustomer.getName()).isEqualTo(newName);
        assertThat(retrievedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(retrievedCustomer.getAge()).isEqualTo(customer.getAge());
    }
}