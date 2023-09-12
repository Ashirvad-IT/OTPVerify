package com.ipsator.serviceImpl;

import java.time.LocalDateTime;
import java.util.Random;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.User;
import com.ipsator.Exception.UserException;
import com.ipsator.Record.OtpDetails;
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
    private  JavaMailSender mailSender;
    
    private final OneTimePasswordRepository otpRepository;

    @Autowired
    public RegistrationServiceImpl(UserRepo userRepository, OneTimePasswordRepository otpRepository,JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.otpRepository=otpRepository;
        this.mailSender=mailSender;
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
        // Generate a random 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(1000000));

        // Set OTP expiration time (e.g., 5 minutes from now)
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        

        // Send the OTP via email
        sendOtpByEmail(user.getEmail(), otp);
        //saving user in OneTimePassword table
        OneTimePassword temporaryUser= new OneTimePassword();
        temporaryUser.setFirstName(user.getFirstName());
        temporaryUser.setLastName(user.getLastName());
        temporaryUser.setEmail(user.getEmail());
        temporaryUser.setAge(user.getAge());
        temporaryUser.setGender(user.getGender());
        temporaryUser.setOtp(otp);
        temporaryUser.setExpirationTime(expirationTime);
        temporaryUser.setStatus("Pending");
        otpRepository.save(temporaryUser);
        OtpDetails otpDetails= new OtpDetails(user.getEmail(),otp,expirationTime);
        return otpDetails.toString();
    }
    private void sendOtpByEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);

        mailSender.send(message);
    }
}
