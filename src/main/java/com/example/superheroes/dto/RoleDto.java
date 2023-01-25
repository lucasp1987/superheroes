package com.example.superheroes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;

@Schema
public record RoleDto(@Schema(description = "Name", type = "string", example = "ROLE_ADMIN") String name,
                      @JsonProperty("privileges") Collection<PrivilegeDto> privilegesDto) {
}