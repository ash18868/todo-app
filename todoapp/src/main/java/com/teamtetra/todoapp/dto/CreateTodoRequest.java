package com.teamtetra.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTodoRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    String title
) {
    public CreateTodoRequest {
        if (title != null) {
            title = title.strip();
        }
    }
}