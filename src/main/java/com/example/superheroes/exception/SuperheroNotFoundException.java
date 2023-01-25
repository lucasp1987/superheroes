package com.example.superheroes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Superhero was not found")
public class SuperheroNotFoundException extends RuntimeException {

    public SuperheroNotFoundException() {
        super();
    }

    public SuperheroNotFoundException(String message) {
        super(message);
    }

}
