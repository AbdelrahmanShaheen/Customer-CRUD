package com.shaheen.customercrud;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CustomercrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomercrudApplication.class, args);
	}
	@Bean
	public CommandLineRunner runner(CustomerRepository customerRepository){
		return args -> {

			Customer alex = new Customer(
					1,
					"Alex",
					"alex@gmail.com",
					21
			);
			Customer jamila = new Customer(
					2,
					"Jamila",
					"jamila@gmail.com",
					24
			);
			customerRepository.saveAll(List.of(alex,jamila));
		};
	}

}
