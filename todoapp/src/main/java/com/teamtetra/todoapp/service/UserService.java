package com.teamtetra.todoapp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.teamtetra.todoapp.entity.User;
import com.teamtetra.todoapp.exception.RegistrationFailure;
import com.teamtetra.todoapp.exception.LoginFailure;
import com.teamtetra.todoapp.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepo userRepo;

    public void registerUser(User user){
        //Check username
        if(isNull(user.getUsername())) throw new RegistrationFailure("Username should not be empty");
        if(!isCorrectLength(user.getUsername())) throw new RegistrationFailure("Username should be between 5 and 15 characters");
        if(isUnique(user.getUsername())) throw new RegistrationFailure("Username must be unique");
        //check password
        if(isNull(user.getPassword())) throw new RegistrationFailure("Password should not be empty");
        if(!isCorrectLength(user.getPassword())) throw new RegistrationFailure("Password should be between 5 and 15 characters");
        if(!hasRequiredCharacters(user.getPassword())) throw new RegistrationFailure("Password requires all special characters");



        userRepo.save(user);
    }

    public User loginUser(User credentials){
        if (isNull(credentials.getUsername()) || isNull(credentials.getPassword())) {
            throw new LoginFailure("Username and password are required");
        }
        Optional<User> userOptional = userRepo.findByUsername(credentials.getUsername());
        //     .orElseThrow(() -> new LoginFailure("Invalid login credentials"));
        // if(!(user.getPassword().equals(registeredUser.getPassword()))){
        //     throw new LoginFailure("Invalid login credentials");
        // }
        if (userOptional.isEmpty()) {
            throw new LoginFailure("Invalid username or password");
        }
        User user = userOptional.get();

        if (!user.getPassword().equals(credentials.getPassword())) {
            throw new LoginFailure("Invalid username or password");
        }

        return user;
    }

    public boolean isCorrectLength(String credential){
        return (credential.length() >=5 && credential.length() <= 15);
    }

    public boolean isNull(String credential){
        return credential == null;
    }

    public boolean hasRequiredCharacters(String credential){
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;

        for(char c : credential.toCharArray()){
            if(Character.isLowerCase(c)) hasLowerCase = true;
            if(Character.isUpperCase(c)) hasUpperCase = true;
            if(Character.isDigit(c)) hasDigit = true;
            if(hasLowerCase && hasUpperCase && hasDigit) return true;
        }
        return false;
    }

    public boolean isUnique(String credential){
        Optional<User> userOptional =  userRepo.findByUsername(credential);
        return userOptional.isPresent();
    }
}
