package com.teamtetra.todoapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teamtetra.todoapp.dto.LoginRequest;
import com.teamtetra.todoapp.dto.RegisterRequest;
import com.teamtetra.todoapp.dto.AuthResponse;
import com.teamtetra.todoapp.exception.RegistrationFailure;
import com.teamtetra.todoapp.exception.LoginFailure;
import com.teamtetra.todoapp.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerNewUser(@Valid @RequestBody RegisterRequest request){
        AuthResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest request){
        AuthResponse response = userService.loginUser(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @ExceptionHandler(RegistrationFailure.class)
    public ResponseEntity<String> handleRegistrationFailure(RegistrationFailure exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(LoginFailure.class)
    public ResponseEntity<String> handleLoginFailure(LoginFailure exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
