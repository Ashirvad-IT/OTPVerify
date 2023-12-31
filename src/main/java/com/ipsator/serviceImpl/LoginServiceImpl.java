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
import com.ipsator.Record.OtpDetails;
import com.ipsator.Repository.EmailOtpRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LoginService;
/**
 * This class implements the LoginService interface and provides methods for user login which
 * including sending one time password on email.
 */
@Service
public class LoginServiceImpl implements LoginService{
	
	@Value("${otp.expire.duration.minutes}")
 	private int otpExpireDuration;
	private JavaMailSender mailSender;
	private EmailOtpRepo emailOtpRepo;
	public LoginServiceImpl(JavaMailSender mailSender, EmailOtpRepo emailOtpRepo) {
		this.mailSender=mailSender;
		this.emailOtpRepo=emailOtpRepo;
	}
	 /**
     * Attempts to initiate the login process by sending an OTP to the specified email address.
     *
     * @param email The email address of the user attempting to log in.
     * @return ServiceResponse<otpDetails> indicating the success or failure of sending the OTP.
     */
	@Override
	public ServiceResponse<OtpDetails> loginUser(String email) {
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
	/**
     * Sends an OTP (One-Time Password) to the specified email address.
     *
     * @param email The email address to which the OTP is sent.
     * @param otp   The OTP code to be sent in the email.
     */
	private void sendOtpByEmail(String email,String otp) {
		SimpleMailMessage message= new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your OTP Code");
		message.setText("Your OTP code is: "+otp);
		mailSender.send(message);
	}
}
