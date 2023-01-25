package com.example.superheroes.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record PrivilegeDto(@Schema(description = "Name", type = "string", example = "PRIVILEGE_WRITE") String name) {
}
