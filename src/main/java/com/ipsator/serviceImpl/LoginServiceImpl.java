package com.ipsator.serviceImpl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.User;
import com.ipsator.Record.OtpDetails;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.payload.UserDto;
import com.ipsator.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{
	
	@Value("${otp.expire.duration.minutes}")
 	private int otpExpireDuration;
	private UserRepo userRepo;
	private JavaMailSender mailSender;
	public LoginServiceImpl(UserRepo userRepo, JavaMailSender mailSender) {
		this.userRepo=userRepo;
		this.mailSender=mailSender;
	}
	@Override
	public ServiceResponse<OtpDetails> loginUser(String email) {
		User user=userRepo.findByEmail(email);
		if(user==null) {
			return new ServiceResponse<>(false,null,"Please sign up first");
		}
		int emailSendAttempts = user.getEmailsendAttempt();
		LocalDateTime emailLock= user.getEmailLockoutUntil();
    	if(emailLock!=null) {
    		LocalDateTime now=LocalDateTime.now();
    		LocalDateTime lockOutTime= user.getEmailLockoutUntil();
    		if(lockOutTime.isAfter(now)) {
    			return new ServiceResponse<>(false,null,"Account is locked till "+user.getEmailLockoutUntil());
    		}else{
    			user.setEmailLockoutUntil(null);
    		} 
    	}
    	if(user!=null && emailSendAttempts > 3) {
    		user.setEmailLockoutUntil(LocalDateTime.now().plusHours(otpExpireDuration));
    		user.setEmailsendAttempt(0);
    	}
    	String otp= String.format("%06d", new Random().nextInt(1000000));
    	user.setOtpExpireTime(LocalDateTime.now().plusMinutes(5));
    	user.setOtp(otp);
		sendOtpByEmail(email, otp);
		emailSendAttempts= emailSendAttempts+1;
		user.setEmailsendAttempt(emailSendAttempts);
		userRepo.save(user);
		return new ServiceResponse<>(true,new OtpDetails(email,otp),"Your otp will expire in 5 minutes");
	}
	private void sendOtpByEmail(String email,String otp) {
		SimpleMailMessage message= new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your OTP Code");
		message.setText("Your OTP code is: "+otp);
		mailSender.send(message);
	}
}
