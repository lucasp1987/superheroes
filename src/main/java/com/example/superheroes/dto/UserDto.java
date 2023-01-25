package com.example.superheroes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Collection;

@Schema
public record UserDto(
        @NotNull @Email @Schema(description = "Email", type = "string", example = "pepeargento@gmail.com") String email,
        @NotNull @Schema(description = "Password", type = "string", example = "Pepe$123") String password,
        @Size(min = 2, max = 50) @Schema(description = "First Name", type = "string", example = "Pepe") String firstName,
        @Size(min = 2, max = 50) @Schema(description = "Last Name", type = "string", example = "Argento") String lastName,
        @Schema(description = "Age", type = "int", example = "19") @Min(18) int age,
        @JsonProperty("roles") Collection<RoleDto> rolesDto) {
}