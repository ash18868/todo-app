package com.teamtetra.todoapp.dto;



public record AuthResponse(
    String token,

    String username,

    Long userId
) {}