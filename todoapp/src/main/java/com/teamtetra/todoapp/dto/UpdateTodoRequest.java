package com.teamtetra.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTodoRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    String title,

    boolean completed
) {
    public UpdateTodoRequest {
        if (title != null) {
            title = title.strip();
        }
    }
}