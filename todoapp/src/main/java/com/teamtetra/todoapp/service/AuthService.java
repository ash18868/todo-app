package com.teamtetra.todoapp.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.teamtetra.todoapp.dto.AuthResponse;
import com.teamtetra.todoapp.dto.LoginRequest;
import com.teamtetra.todoapp.dto.RegisterRequest;
import com.teamtetra.todoapp.entity.User;
import com.teamtetra.todoapp.exception.RegistrationFailure;
import com.teamtetra.todoapp.exception.LoginFailure;
import com.teamtetra.todoapp.repo.UserRepo;
import com.teamtetra.todoapp.utility.JwtUtility;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;

    public AuthResponse registerUser(RegisterRequest request){
        // Check username
        if(isUnique(request.username())) throw new RegistrationFailure("Username must be unique");
        // Check password
        if(!hasRequiredCharacters(request.password())) throw new RegistrationFailure("Password must include at least one uppercase, one lowercase, one number, one special character");

        // Build user
        User user = User.builder()
                    .username(request.username())
                    .password(passwordEncoder.encode(request.password()))
                    .build();

        // Save user to DB
        User savedUser = userRepo.save(user);

        // Do not generate a token at this time, user will have to navigate back to the login page
        return new AuthResponse("No-token", savedUser.getUsername(), savedUser.getUserId());
        
    }

    public AuthResponse loginUser(LoginRequest request){

        // Checks if user exists
        Optional<User> userOptional = userRepo.findByUsername(request.username());
        if (userOptional.isEmpty()) {
            throw new LoginFailure("Invalid username or password"); 
        }

        User user = userOptional.get(); // convert to user

        // Verify if password is correct
        if(!passwordEncoder.matches(request.password(), user.getPassword())) {throw new LoginFailure("Invalid username or password");}
        
        String token = jwtUtility.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getUserId());
        
    }

    public Long extractUserId(HttpServletRequest request){
        String userIdFromToken = (String) request.getAttribute("userId");
        return Long.parseLong(userIdFromToken);
    }

    private boolean hasRequiredCharacters(String credential){
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        for(char c : credential.toCharArray()){
            if(Character.isLowerCase(c)) hasLowerCase = true;
            if(Character.isUpperCase(c)) hasUpperCase = true;
            if(Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {hasSymbol = true;}
            if(hasLowerCase && hasUpperCase && hasDigit && hasSymbol) return true;
        }
        return false;
    }

    private boolean isUnique(String credential){
        Optional<User> userOptional =  userRepo.findByUsername(credential);
        return userOptional.isPresent();
    }
}
