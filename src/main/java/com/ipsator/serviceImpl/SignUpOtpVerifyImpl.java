package com.ipsator.serviceImpl;
import java.util.*;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.TemporaryUser;
import com.ipsator.Entity.User;
import com.ipsator.Record.OtpDetails;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.TemporaryUserRepo;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.SignUpOtpVerify;
/**
 * 
 * @author Ashirvad Kumar
 * This class provide the implementation of OneTimePasswordService interface and provide the implementation to 
 * generateOtp method
 */
@Service
public class SignUpOtpVerifyImpl implements SignUpOtpVerify {
	
	 	private  OneTimePasswordRepository otpRepository;
	 	private UserRepo userRepo;
	    private PasswordEncoder passwordEncoder;
	    private TemporaryUserRepo temporaryUserRepo;

	    @Autowired
	    public SignUpOtpVerifyImpl(OneTimePasswordRepository otpRepository, UserRepo userRepo,PasswordEncoder passwordEncoder,TemporaryUserRepo temporaryUserRepo) {
	        this.otpRepository = otpRepository;
	        this.userRepo=userRepo;
	        this.passwordEncoder=passwordEncoder;
	        this.temporaryUserRepo=temporaryUserRepo;
	    }
    	/**
    	 * This method is responsible to generate otp it will take email and then generate otp 
    	 * 
    	 * @param email It is string data type
    	 * @return It will return OtpDetails record containing otp and expiry time of the otp 
    	 * @throws Exception
    	 */
	    public ServiceResponse<User> verifyOTP(String email,String otp){
	    	
	    	OneTimePassword otpInformation= otpRepository.findByEmail(email);
	    	
	    	/**
	    	 * Here user has not sign up and trying to generate otp
	    	 */
	    	if(otpInformation==null) {
	    		return new ServiceResponse(false,null,"Please sign up first");
	    	}
	    	/**
	    	 * Here we are checking i.e user enter the otp before the expiry time
	    	 * otherwise he will get an exception
	    	 */
	    	if(otpInformation!=null && otpInformation.getLockoutEndTime() != null) {
	    		LocalDateTime currentTime= LocalDateTime.now();//10:14
	    		LocalDateTime lockOutEndTime= otpInformation.getLockoutEndTime();//10:15
	    		if(currentTime.isBefore(lockOutEndTime)) {
	    			return new ServiceResponse<User>(false, null, "Please try after "+otpInformation.getLockoutEndTime());
	    		}else {
	    			otpInformation.setOtpAttempts(0);
	    			otpInformation.setLockoutEndTime(null);
	    		}
	    	}
	        if(otpInformation==null) {
	        	otpInformation=new OneTimePassword();
	        }
	    	if (!otpInformation.getOtp().equals(otp)) {
	        	//If the user is enters wrong otp then we will increase the otpAttempts
	    		otpInformation.setOtpAttempts(otpInformation.getOtpAttempts()+1);//2
	            otpRepository.save(otpInformation);
	            if(otpInformation.getOtpAttempts()<5) {
	            	return new ServiceResponse<User>(false, null, "Invalid Otp");
	            }
	        }
	    	
	    	//If the user has reached or exceed its otp attempts 
	        if(otpInformation.getOtpAttempts()>=5) {
	        	//Lock the user for 3 hours because it has reach its maximum attempts
	        	LocalDateTime lockOutEndTIme=LocalDateTime.now().plusHours(3);
	        	otpInformation.setLockoutEndTime(lockOutEndTIme);
//	        	temporaryUser.setOtpAttempts(0);
	        	otpRepository.save(otpInformation);
	        	return new ServiceResponse<User>(false, null, "Maximum Otp attempts reached.");
	        }
	    	
	    	if(otpInformation.getExpirationTime()!=null && otpInformation.getExpirationTime().isBefore(LocalDateTime.now())) {
	    		return new ServiceResponse<User>(false, null,"Otp has expire");
	    	}
	    	otpInformation.setExpirationTime(null);
	    	otpInformation.setLockoutEndTime(null);
	    	otpInformation.setOtpAttempts(0);
//	    	otpInformation.setSignUpTime(LocalDateTime.now());
	    	
	    	//
	    	TemporaryUser temporaryUser=temporaryUserRepo.findByEmail(email);
	    	temporaryUser.setSignUpTime(LocalDateTime.now());
	    	// saving temporary user into user table
	    	User user= new User();
	    	user.setAge(temporaryUser.getAge());
	    	user.setEmail(temporaryUser.getEmail());
	    	user.setFirstName(temporaryUser.getFirstName());
	    	user.setLastName(temporaryUser.getLastName());
	    	user.setGender(temporaryUser.getGender());
	    	user.setPassword(passwordEncoder.encode(otpInformation.getOtp()));
	    	otpInformation.setOtp(otp);
	    	otpRepository.save(otpInformation);
	    	userRepo.save(user);
	    	return new ServiceResponse<User>(true,user ,"Sign up success");
	    }

	    
}
