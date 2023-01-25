package com.example.superheroes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class SuperheroesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperheroesApplication.class, args);
    }

}
