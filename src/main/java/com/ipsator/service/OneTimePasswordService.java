package com.ipsator.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Repository.OneTimePasswordRepository;

@Service
public class OneTimePasswordService {
	 private  OneTimePasswordRepository otpRepository;
	    private  JavaMailSender mailSender;

	    @Autowired
	    public OneTimePasswordService(OneTimePasswordRepository otpRepository, JavaMailSender mailSender) {
	        this.otpRepository = otpRepository;
	        this.mailSender = mailSender;
	    }

	    public OneTimePassword generateOTP(String email) {
	        // Generate a random 6-digit OTP
	        String otp = String.format("%06d", new Random().nextInt(1000000));

	        // Set OTP expiration time (e.g., 5 minutes from now)
	        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

	        // Save the OTP in the database
	        OneTimePassword otpEntity = new OneTimePassword();
	        otpEntity.setEmail(email);
	        otpEntity.setOtp(otp);
	        otpEntity.setExpirationTime(expirationTime);
	        otpRepository.save(otpEntity);

	        // Send the OTP via email
	        sendOtpByEmail(email, otp);

	        return otpEntity;
	    }

	    private void sendOtpByEmail(String email, String otp) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(email);
	        message.setSubject("Your OTP Code");
	        message.setText("Your OTP code is: " + otp);

	        mailSender.send(message);
	    }
}
