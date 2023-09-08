package com.ipsator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.User;
import com.ipsator.Repository.UserRepo;

@Service
public class RegistrationService {
    private final UserRepo userRepository;

    @Autowired
    public RegistrationService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }
    
    public String registerUser(User user) {
        // Check if the email is already registered
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }

        // Create a new user with a generated ID
        
        // Save the user to the database
        userRepository.save(user);
        return "Sign up sucessfully";
    }
}
