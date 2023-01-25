package com.example.superheroes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = """
        Password is not valid, please check this:                        
         • Password must contain at least one digit [0-9].
         • Password must contain at least one lowercase Latin character [a-z].
         • Password must contain at least one uppercase Latin character [A-Z].
         • Password must contain at least one special character like ! @ # & ( ).
         • Password must contain a length of at least 8 characters and a maximum of 20 characters.
        """)
public class PasswordInvalidException extends RuntimeException {
}
