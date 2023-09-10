package com.ipsator.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.User;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.UserRepo;

@Service
public class OneTimePasswordService {
	 private  OneTimePasswordRepository otpRepository;
	    private  JavaMailSender mailSender;
	    private UserRepo userRepo;

	    @Autowired
	    public OneTimePasswordService(OneTimePasswordRepository otpRepository, JavaMailSender mailSender,UserRepo userRepo) {
	        this.otpRepository = otpRepository;
	        this.mailSender = mailSender;
	        this.userRepo=userRepo;
	    }

	    public OneTimePassword generateOTP(String email) {
	    	
	    	OneTimePassword temporaryUser= otpRepository.findByEmail(email);
	    	
	    	/**
	    	 * Here user has not sign up and trying to generate otp
	    	 */
	    	if(temporaryUser==null) {
	    		throw new RuntimeException("Please sign up first");
	    	}
	    	/**
	    	 * Here we are checking i.e user enter the otp before the expiry time
	    	 * otherwise he will get an exception
	    	 */
	    	if(temporaryUser!=null && temporaryUser.getLockoutEndTime() != null) {
	    		LocalDateTime currentTime= LocalDateTime.now();
	    		LocalDateTime lockOutEndTime= temporaryUser.getLockoutEndTime();
	    		if(currentTime.isBefore(lockOutEndTime)) {
	    			throw new RuntimeException("Please try after "+temporaryUser.getLockoutEndTime());
	    		}else {
	    			temporaryUser.setOtpAttempts(0);
	    			temporaryUser.setLockoutEndTime(null);
	    		}
	    	}
	        // Generate a random 6-digit OTP
	        String otp = String.format("%06d", new Random().nextInt(1000000));

	        // Set OTP expiration time (e.g., 5 minutes from now)
	        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

	        // Save the OTP in the database
	        
	    
	        temporaryUser.setOtp(otp);
	        temporaryUser.setExpirationTime(expirationTime);
	        otpRepository.save(temporaryUser);

	        // Send the OTP via email
	        sendOtpByEmail(email, otp);

	        return temporaryUser;
	    }

	    private void sendOtpByEmail(String email, String otp) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(email);
	        message.setSubject("Your OTP Code");
	        message.setText("Your OTP code is: " + otp);

	        mailSender.send(message);
	    }
}
