package com.ipsator.serviceImpl;

import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.ipsator.Entity.User;
import com.ipsator.Record.OtpDetails;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.payload.UserDto;
import com.ipsator.service.SignUpService;
/**
 * 
 * @author Ashirvad Kumar
 * @since 2023
 * This is RegisterService class, here we are saving the details of user in a temporary table
 * i.e. OneTimePassword table. Because there may be a condition where user have sign up and after
 * that he will not able to enter the right otp.
 */
@Service
public class SignUpServiceImpl implements SignUpService {
	
	@Value("${otp.expire.duration.minutes}")
 	private int otpExpireDuration;
    private final UserRepo userRepo;
    private final JavaMailSender mailSender;
    @Autowired
    public SignUpServiceImpl(UserRepo userRepository,JavaMailSender mailSender) {
        this.userRepo = userRepository;
        this.mailSender=mailSender;
    }
    
   
    public ServiceResponse<OtpDetails> registerUser(UserDto userDto) throws Exception{
        // Check if the email is already registered
		User user=userRepo.findByEmail(userDto.getEmail());
		//If the user come first time to sign up
		if(user==null) {
			user=new User();
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
		sendOtpByEmail(userDto.getEmail(), otp);
		emailSendAttempts= emailSendAttempts+1;
		user.setEmailsendAttempt(emailSendAttempts);
		user.setAge(userDto.getAge());
		user.setEmail(userDto.getEmail());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		userRepo.save(user);
    	return new ServiceResponse(true, new OtpDetails(userDto.getEmail(),otp),"Otp will expire in 5 minutes");
    }
    
    private void sendOtpByEmail(String email,String otp) {
    	SimpleMailMessage message= new SimpleMailMessage();
    	message.setTo(email);
    	message.setSubject("Your otp code");
    	message.setText("Your otp code is: "+otp);
    	mailSender.send(message);
    }
    
    
}
