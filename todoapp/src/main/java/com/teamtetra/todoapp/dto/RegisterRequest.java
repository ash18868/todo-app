package com.teamtetra.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Username should not be empty")
    @Size(min = 5, max = 15, message = "Username should be between 5 and 15 characters")
    String username,

    @NotBlank(message = "Password should not be empty")
    @Size(min = 5, max = 15, message = "Password should be between 5 and 15 characters")
    String password
) {}
