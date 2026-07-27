package com.teamtetra.todoapp.dto;

public record SubtaskResponse(
    Long subtaskId,
    Long todoId,
    String title,
    boolean completed
) {}
