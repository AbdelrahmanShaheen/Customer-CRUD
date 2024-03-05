package com.shaheen.customercrud;

import com.shaheen.customercrud.dao.CustomerDao;
import com.shaheen.customercrud.entity.Customer;
import com.shaheen.customercrud.requestEntity.CustomerRegistrationRequest;
import com.shaheen.customercrud.requestEntity.CustomerUpdateRequest;
import com.shaheen.customercrud.service.CustomerService;
import com.shaheen.customercrud.exception.DuplicateResourceException;
import com.shaheen.customercrud.exception.RequestValidationException;
import com.shaheen.customercrud.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class CustomerServiceTest {
    private CustomerService underTest;
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        customerDao = mock(CustomerDao.class);
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomersShouldSuccess() {
        // when
        underTest.getAllCustomers();
        // then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomerShouldSuccessIfIdIsPresent() {
        // given
        Integer id = 5;
        String email = "ahmed1@gmail.com";
        Customer customer = new Customer(5,"Ahmed" ,email ,22);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // when
        Customer customer1 = underTest.getCustomer(5);
        // then
        assertThat(customer1).isEqualTo(customer);
    }
    @Test
    void getCustomerShouldThrowErrorIfIdIsNotPresent() {
        // given
        Integer id = 5;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomerShouldSuccess() {
        // given
        String email = "shaheen.com";
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(
                        "Abdelrahman",
                        email,
                        24
                );
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        // when
        underTest.addCustomer(request);
        // then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(email);
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }
    @Test
    void addCustomerShouldThrowErrorIfEmailIsExist() {
        // given
        String email = "shaheen.com";
        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(
                        "Abdelrahman",
                        email,
                        24
                );
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);
        // then
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email already taken");
        verify(customerDao ,never()).insertCustomer(any());
    }
    @Test
    void removeCustomerShouldSuccessIfIdIsPresent() {
        // given
        Integer id = 5;
        when(customerDao.existsPersonWithId(id)).thenReturn(true);
        // when
        underTest.removeCustomer(id);
        // then
        verify(customerDao).deleteCustomerById(id);
    }
    @Test
    void removeCustomerShouldThrowExceptionIfIdIsNotPresent() {
        // given
        Integer id = 5;
        when(customerDao.existsPersonWithId(id)).thenReturn(false);
        // when ,then
        assertThatThrownBy(()-> underTest.removeCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("customer with id [%s] not found".formatted(id));
        verify(customerDao ,never()).deleteCustomerById(id);
    }
    @Test
    void updateAllCustomerPropertiesShouldSuccess() {
        // given
        Integer id = 5;
        String email = "shaheenabdelrahman28@gmail.com";
        CustomerUpdateRequest request =
                new CustomerUpdateRequest("Abdelrahman Shaheen",email,23);
        Customer customer = new Customer(id,"shaheen" ,"shaheen@gmail.com" ,24);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        // when
        underTest.updateCustomer(id ,request);
        // then
        assertThat(customer.getName()).isEqualTo(request.name());
        assertThat(customer.getEmail()).isEqualTo(request.email());
        assertThat(customer.getAge()).isEqualTo(request.age());
        verify(customerDao).updateCustomer(customer);
    }
    @Test
    void updateOnlyCustomerNameShouldSuccess() {
        // given
        Integer id = 5;
        String name = "shaheen";
        String email = "shaheen@gmail.com";
        Integer age = 23;
        CustomerUpdateRequest request =
                new CustomerUpdateRequest("Abdelrahman Shaheen",null,null);
        Customer customer = new Customer(id, name, email,age);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // when
        underTest.updateCustomer(id ,request);
        // then
        assertThat(customer.getName()).isEqualTo(request.name());
        assertThat(customer.getEmail()).isEqualTo(email);
        assertThat(customer.getAge()).isEqualTo(age);
        verify(customerDao).updateCustomer(customer);
    }
    @Test
    void updateOnlyCustomerEmailShouldSuccess() {
        // given
        Integer id = 5;
        String name = "shaheen";
        String email = "shaheen@gmail.com";
        Integer age = 23;
        CustomerUpdateRequest request =
                new CustomerUpdateRequest(null,"shaheenabdelrahman28@gmail.com",null);
        Customer customer = new Customer(id, name, email, age);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        // when
        underTest.updateCustomer(id ,request);
        // then
        assertThat(customer.getName()).isEqualTo(name);
        assertThat(customer.getEmail()).isEqualTo(request.email());
        assertThat(customer.getAge()).isEqualTo(age);
        verify(customerDao).updateCustomer(customer);
    }
    @Test
    void updateOnlyCustomerAgeShouldSuccess() {
        // given
        Integer id = 5;
        String name = "shaheen";
        String email = "shaheen@gmail.com";
        Integer age = 23;
        CustomerUpdateRequest request =
                new CustomerUpdateRequest(null,null,24);
        Customer customer = new Customer(id, name, email, age);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // when
        underTest.updateCustomer(id ,request);
        // then
        assertThat(customer.getName()).isEqualTo(name);
        assertThat(customer.getEmail()).isEqualTo(email);
        assertThat(customer.getAge()).isEqualTo(request.age());
        verify(customerDao).updateCustomer(customer);
    }
    @Test
    void updateCustomerShouldThrowExceptionIfEmailIsPresent() {
        // given
        Integer id = 5;
        String name = "shaheen";
        String email = "shaheen@gmail.com";
        String newEmail = "shaheenabdelrahman28@gmail.com";
        Integer age = 23;
        CustomerUpdateRequest request =
                new CustomerUpdateRequest(null, newEmail,null);
        Customer customer = new Customer(id, name, email, age);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);
        // when ,then
        assertThatThrownBy(() -> underTest.updateCustomer(id ,request))
                            .isInstanceOf(DuplicateResourceException.class)
                            .hasMessageContaining("email already taken");
        verify(customerDao ,never()).updateCustomer(any());
    }
    @Test
    void updateCustomerShouldThrowExceptionIfNoUpdatesArePresent() {
        // given
        Integer id = 5;
        String name = "shaheen";
        String email = "shaheen@gmail.com";
        Integer age = 23;
        CustomerUpdateRequest request =
                new CustomerUpdateRequest(null, null,null);
        Customer customer = new Customer(id, name, email, age);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // when ,then
        assertThatThrownBy(() -> underTest.updateCustomer(id ,request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("no data changes found");
        verify(customerDao ,never()).updateCustomer(any());
    }
}