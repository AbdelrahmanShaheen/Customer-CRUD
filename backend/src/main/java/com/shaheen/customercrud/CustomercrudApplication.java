package com.shaheen.customercrud;

import com.github.javafaker.Faker;
import com.shaheen.customercrud.entity.Customer;
import com.shaheen.customercrud.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class CustomercrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomercrudApplication.class, args);
	}
	@Bean
	public CommandLineRunner runner(CustomerRepository customerRepository){
		return args -> {
			var faker = new Faker();
			var name = faker.name();
			Random random = new Random();
			var customer = new Customer(
				name.firstName() + " " + name.lastName(),
					name.firstName().toLowerCase() + "." + name.lastName().toLowerCase() + "@gmail.com",
				random.nextInt(16,99)
					);
			customerRepository.save(customer);
		};
	}
}
