package com.ipsator.serviceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.EmailOtp;
import com.ipsator.Entity.User;
import com.ipsator.Record.OtpDetails;
import com.ipsator.Repository.EmailOtpRepo;
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
	private EmailOtpRepo emailOtpRepo;
	public LoginServiceImpl(UserRepo userRepo, JavaMailSender mailSender, EmailOtpRepo emailOtpRepo) {
		this.userRepo=userRepo;
		this.mailSender=mailSender;
		this.emailOtpRepo=emailOtpRepo;
	}
	@Override
	public ServiceResponse<OtpDetails> loginUser(String email) {
		User user=userRepo.findByEmail(email);
		if(user==null) { 
			return new ServiceResponse<>(false,null,"User with this email already exist");
		}
		Optional<EmailOtp> opt= emailOtpRepo.findByEmail(email);
		EmailOtp newUserOtpEmailDetails;
		if(opt.isPresent() && opt.get().getEmailsendAttempt() >= 3) {
			newUserOtpEmailDetails= opt.get();
			newUserOtpEmailDetails.setEmailLockoutUntil(LocalDateTime.now().plusHours(otpExpireDuration));
			newUserOtpEmailDetails.setEmailsendAttempt(0);
			emailOtpRepo.save(newUserOtpEmailDetails);
    	}
		if(opt.isPresent()) {
			newUserOtpEmailDetails= opt.get();
			int emailSendAttempts = newUserOtpEmailDetails.getEmailsendAttempt();
			LocalDateTime emailLock= newUserOtpEmailDetails.getEmailLockoutUntil();
			if(emailLock!=null) {
	    		LocalDateTime now=LocalDateTime.now();
	    		LocalDateTime lockOutTime= newUserOtpEmailDetails.getEmailLockoutUntil();
	    		if(lockOutTime.isAfter(now)) {
	    			return new ServiceResponse<>(false,null,"Account is locked till "+newUserOtpEmailDetails.getEmailLockoutUntil());
	    		}else{
	    			newUserOtpEmailDetails.setEmailLockoutUntil(null);
	    		} 
	    	}
	    			}
		else {
			newUserOtpEmailDetails= new EmailOtp();
		}
		String otp=null;
		if(newUserOtpEmailDetails.getLastEmailSend()!=null && ChronoUnit.MINUTES.between(newUserOtpEmailDetails.getLastEmailSend(), LocalDateTime.now())<=5){
			otp=newUserOtpEmailDetails.getOtp();
			sendOtpByEmail(email, newUserOtpEmailDetails.getOtp());
		}else {
			otp= String.format("%06d", new Random().nextInt(1000000));
			newUserOtpEmailDetails.setOtp(otp);
			sendOtpByEmail(email, otp);
			newUserOtpEmailDetails.setLastEmailSend(LocalDateTime.now());
			newUserOtpEmailDetails.setOtpExpireTime(LocalDateTime.now().plusMinutes(5)); 
		}
		newUserOtpEmailDetails.setEmailsendAttempt(newUserOtpEmailDetails.getEmailsendAttempt()+1);
		newUserOtpEmailDetails.setEmail(email);
		emailOtpRepo.save(newUserOtpEmailDetails);
    	return new ServiceResponse(true, new OtpDetails(email,otp),"Otp send successfully");
	}
	private void sendOtpByEmail(String email,String otp) {
		SimpleMailMessage message= new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your OTP Code");
		message.setText("Your OTP code is: "+otp);
		mailSender.send(message);
	}
}
