package com.example.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@SpringBootApplication
public class RestApiApplication {

	@GetMapping("/hi")
	Flux<String> hi() {
		return Flux.just("Hello, world!");
	}

	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}
}
