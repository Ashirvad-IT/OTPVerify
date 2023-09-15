package com.ipsator.serviceImpl;
import java.util.*;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.User;
import com.ipsator.Exception.OneTimePasswordException;
import com.ipsator.Exception.UserException;
import com.ipsator.Record.OtpDetails;
import com.ipsator.Repository.OneTimePasswordRepository;
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

	    @Autowired
	    public SignUpOtpVerifyImpl(OneTimePasswordRepository otpRepository, UserRepo userRepo) {
	        this.otpRepository = otpRepository;
	        this.userRepo=userRepo;
	    }
    	/**
    	 * This method is responsible to generate otp it will take email and then generate otp 
    	 * 
    	 * @param email It is string data type
    	 * @return It will return OtpDetails record containing otp and expiry time of the otp 
    	 * @throws Exception
    	 */
	    public ServiceResponse<User> verifyOTP(String email,String otp){
	    	
	    	OneTimePassword temporaryUser= otpRepository.findByEmail(email);
	    	
	    	/**
	    	 * Here user has not sign up and trying to generate otp
	    	 */
	    	if(temporaryUser==null) {
	    		return new ServiceResponse(false,null,"Please sign up first");
	    	}
	    	/**
	    	 * Here we are checking i.e user enter the otp before the expiry time
	    	 * otherwise he will get an exception
	    	 */
	    	if(temporaryUser!=null && temporaryUser.getLockoutEndTime() != null) {
	    		LocalDateTime currentTime= LocalDateTime.now();//10:14
	    		LocalDateTime lockOutEndTime= temporaryUser.getLockoutEndTime();//10:15
	    		if(currentTime.isBefore(lockOutEndTime)) {
	    			return new ServiceResponse<User>(false, null, "Please try after "+temporaryUser.getLockoutEndTime());
	    		}else {
	    			temporaryUser.setOtpAttempts(0);
	    			temporaryUser.setLockoutEndTime(null);
	    		}
	    	}
	        
	    	if (!temporaryUser.getOtp().equals(otp)) {
	        	//If the user is enters wrong otp then we will increase the otpAttempts
	            temporaryUser.setOtpAttempts(temporaryUser.getOtpAttempts()+1);//2
	            otpRepository.save(temporaryUser);
	            if(temporaryUser.getOtpAttempts()<5) {
	            	return new ServiceResponse<User>(false, null, "Invalid Otp");
	            }
	        }
	    	
	    	//If the user has reached or exceed its otp attempts 
	        if(temporaryUser.getOtpAttempts()>=5) {
	        	//Lock the user for 3 hours because it has reach its maximum attempts
	        	LocalDateTime lockOutEndTIme=LocalDateTime.now().plusHours(3);
	        	temporaryUser.setLockoutEndTime(lockOutEndTIme);
//	        	temporaryUser.setOtpAttempts(0);
	        	otpRepository.save(temporaryUser);
	        	return new ServiceResponse<User>(false, null, "Maximum Otp attempts reached.");
	        }
	    	
	    	if(temporaryUser.getExpirationTime()!=null && temporaryUser.getExpirationTime().isBefore(LocalDateTime.now())) {
	    		return new ServiceResponse<User>(false, null,"Otp has expire");
	    	}
	    	temporaryUser.setExpirationTime(null);
	    	temporaryUser.setLockoutEndTime(null);
	    	temporaryUser.setOtpAttempts(0);
	    	temporaryUser.setSignUpTime(LocalDateTime.now());
	    	otpRepository.save(temporaryUser);
	    	// saving temporary user into user table
	    	User user= new User();
	    	user.setId(temporaryUser.getId());
	    	user.setAge(temporaryUser.getAge());
	    	user.setEmail(temporaryUser.getEmail());
	    	user.setFirstName(temporaryUser.getFirstName());
	    	user.setLastName(temporaryUser.getLastName());
	    	user.setGender(temporaryUser.getGender());
	    	userRepo.save(user);
	    	return new ServiceResponse<User>(true,user ,"Sign up success");
	    }

	    
}
