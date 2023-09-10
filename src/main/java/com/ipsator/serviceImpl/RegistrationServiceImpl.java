package com.ipsator.serviceImpl;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.User;
import com.ipsator.Exception.UserException;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.UserRepo;
import com.ipsator.service.RegistrationService;
/**
 * 
 * @author Ashirvad Kumar
 * @since 2023
 * This is RegisterService class, here we are saving the details of user in a temporary table
 * i.e. OneTimePassword table. Because there may be a condition where user have sign up and after
 * that he will not able to enter the right otp.
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final UserRepo userRepository;
    
    private final OneTimePasswordRepository otpRepository;

    @Autowired
    public RegistrationServiceImpl(UserRepo userRepository, OneTimePasswordRepository otpRepository) {
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
    public String registerUser(User user) throws Exception{
        // Check if the email is already registered
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserException("Email already registered");
        }
        if(otpRepository.findByEmail(user.getEmail())!=null) {
        	throw new UserException("You have already sign up. you are eligible to generate otp");
        }
        OneTimePassword userDetails= new OneTimePassword();
        userDetails.setFirstName(user.getFirstName());
        userDetails.setLastName(user.getLastName());
        userDetails.setEmail(user.getEmail());
        userDetails.setAge(user.getAge());
        userDetails.setGender(user.getGender());
        otpRepository.save(userDetails);
        return "Sign up sucessfully";
    }
}
