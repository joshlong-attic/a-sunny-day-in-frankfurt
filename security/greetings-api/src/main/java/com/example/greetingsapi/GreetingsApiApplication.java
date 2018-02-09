package com.example.greetingsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@EnableResourceServer
@SpringBootApplication
public class GreetingsApiApplication {

	@GetMapping("/hi")
	String hi(Principal principal) {
		return "Hallo " + principal.getName();
	}

	public static void main(String[] args) {
		SpringApplication.run(GreetingsApiApplication.class, args);
	}
}
