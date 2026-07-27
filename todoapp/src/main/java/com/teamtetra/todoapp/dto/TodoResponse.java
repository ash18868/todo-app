package com.teamtetra.todoapp.dto;

public record TodoResponse(
    Long todoId,
    String title,
    boolean completed
) {}