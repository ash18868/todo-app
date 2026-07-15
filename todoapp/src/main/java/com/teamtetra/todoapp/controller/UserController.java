package com.teamtetra.todoapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teamtetra.todoapp.entity.User;
import com.teamtetra.todoapp.exception.RegistrationFailure;
import com.teamtetra.todoapp.exception.LoginFailure;
import com.teamtetra.todoapp.service.UserService;
import com.teamtetra.todoapp.utility.JwtUtility;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final JwtUtility jwtUtility;

    @PostMapping("/register")
    public ResponseEntity<Void> registerNewUser(@RequestBody User user){
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User credentials){
        User user = userService.loginUser(credentials);
        String token = jwtUtility.generateToken(user);
        return ResponseEntity.ok(token);
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
