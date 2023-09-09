package com.ipsator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.User;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.UserRepo;
/**
 * 
 * @author Ashirvad Kumar
 * @since 2023
 * This is RegisterService class, here we are saving the details of user in a temporary table
 * i.e. OneTimePassword table. Because there may be a condition where user have sign up and after
 * that he will not able to enter the right otp.
 */
@Service
public class RegistrationService {
    private final UserRepo userRepository;
    
    private final OneTimePasswordRepository otpRepository;

    @Autowired
    public RegistrationService(UserRepo userRepository, OneTimePasswordRepository otpRepository) {
        this.userRepository = userRepository;
        this.otpRepository=otpRepository;
    }
    
    /**
     *Here we are first checking user with this email is already exist or not.If user with this id is already
     *there then we will throw an Exception. If it is a new User then we will save the details.
     *@param user This will be the object of user class containing the details of user like first name,last name,
     *email etc.
     *@return It will return a string if the user has successfully register
     *@throws
     */
    public String registerUser(User user) {
        // Check if the email is already registered
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }

        OneTimePassword temporaryDetails= new OneTimePassword();
        temporaryDetails.setFirstName(user.getFirstName());
        temporaryDetails.setLastname(user.getLastName());
        temporaryDetails.setEmail(user.getEmail());
        
        otpRepository.save(temporaryDetails);
        return "Sign up sucessfully";
    }
}