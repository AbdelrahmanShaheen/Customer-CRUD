package com.shaheen.journey;

import com.github.javafaker.*;
import com.shaheen.customercrud.entity.Customer;
import com.shaheen.customercrud.requestEntity.CustomerRegistrationRequest;
import com.shaheen.customercrud.requestEntity.CustomerUpdateRequest;
import com.shaheen.customercrud.CustomercrudApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CustomercrudApplication.class,webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    private final Faker faker = new Faker();
    private final Random random = new Random();
    private static final String CUSTOMER_URI = "/customers";
    @Test
    void canRegisterACustomer() {
        // create registration request
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@shaheen.com";
        int age = random.nextInt(10 ,100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name ,email ,age);
        // send a post request
        webTestClient
                .post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request) ,CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
        // get all customers
        List<Customer> customers = webTestClient
                .get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                }).returnResult()
                .getResponseBody();
        // make sure that customer is present
        Customer customer = new Customer(name ,email ,age);
        assertThat(customers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(customer);

        int id = customers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        customer.setId(id);
        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{customerId}" ,id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                }).isEqualTo(customer);
    }
    @Test
    void canDeleteCustomer(){
        // create registration request
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@shaheen.com";
        int age = random.nextInt(10 ,100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name ,email ,age);
        // send a post request
        webTestClient
                .post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request) ,CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
        // get all customers
        List<Customer> customers = webTestClient
                .get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                }).returnResult()
                .getResponseBody();

        int id = customers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // delete the customer
        webTestClient
                .delete()
                .uri(CUSTOMER_URI + "/{customerId}" ,id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{customerId}" ,id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
    @Test
    void canUpdateCustomer(){
        // create registration request
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@shaheen.com";
        int age = random.nextInt(10 ,100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name ,email ,age);
        // send a post request
        webTestClient
                .post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request) ,CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
        // get all customers
        List<Customer> customers = webTestClient
                .get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                }).returnResult()
                .getResponseBody();

        int id = customers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // update the customer
        String newName = "new name";
        CustomerUpdateRequest updatedRequest = new CustomerUpdateRequest(newName ,null ,null);
        webTestClient
                .put()
                .uri(CUSTOMER_URI + "/{customerId}" ,id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedRequest) ,CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                                .uri(CUSTOMER_URI + "/{customerId}" ,id)
                                .accept(MediaType.APPLICATION_JSON)
                                .exchange()
                                .expectStatus()
                                .isOk()
                                .expectBody(new ParameterizedTypeReference<Customer>() {
                                }).returnResult()
                                .getResponseBody();
        Customer customer = new Customer(id ,newName ,email ,age);
        assertThat(updatedCustomer).isEqualTo(customer);
    }
}
