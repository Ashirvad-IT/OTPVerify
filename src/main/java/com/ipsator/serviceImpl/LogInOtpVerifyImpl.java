package com.ipsator.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.User;
import com.ipsator.Exception.OneTimePasswordException;
import com.ipsator.Exception.UserException;
import com.ipsator.Record.UserDetails;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LogInOtpVerify;

@Service
public class LogInOtpVerifyImpl implements LogInOtpVerify{
	
	private  OneTimePasswordRepository otpRepository;
	private UserRepo userRepository;
	
	@Autowired
	public LogInOtpVerifyImpl(OneTimePasswordRepository otpRepository,UserRepo userRepository) {
		super();
		this.otpRepository = otpRepository;
		this.userRepository=userRepository;
	}

	@Override
	public ServiceResponse<User> otpVerify(String email, String otp){
		
		OneTimePassword temporaryUser= otpRepository.findByEmail(email);
		
		if(temporaryUser==null) {
    		ServiceResponse<User> response= new ServiceResponse<>(false,null,"Please, sign up first");
    		return response;
    	}
		
		if(temporaryUser!=null && temporaryUser.getLockoutEndTime() != null) {
    		LocalDateTime currentTime= LocalDateTime.now();
    		LocalDateTime lockOutEndTime= temporaryUser.getLockoutEndTime();
    		if(currentTime.isBefore(lockOutEndTime)) {
    			ServiceResponse<User> response=new ServiceResponse<>(false,null,"Please try after "+temporaryUser.getLockoutEndTime());;
    			return response;
    		}else {
    			temporaryUser.setOtpAttempts(0);
    			temporaryUser.setLockoutEndTime(null);
    		}
    	}
        
    	if (!temporaryUser.getOtp().equals(otp)) {
        	//If the user is enters wrong otp then we will increase the otpAttempts
            temporaryUser.setOtpAttempts(temporaryUser.getOtpAttempts()+1);
            otpRepository.save(temporaryUser);
            if(temporaryUser.getOtpAttempts()<5) {
            	ServiceResponse<User> response= new ServiceResponse<>(false,null,"Invalid Otp");
            	return response;
            }
        }
    	
    	//If the user has reached or exceed its otp attempts 
        if(temporaryUser.getOtpAttempts()>=5) {
        	//Lock the user for 3 hours because it has reach its maximum attempts
        	LocalDateTime lockOutEndTIme=LocalDateTime.now().plusHours(3);
        	temporaryUser.setLockoutEndTime(lockOutEndTIme);
//        	temporaryUser.setOtpAttempts(0);
        	otpRepository.save(temporaryUser);
        	ServiceResponse<User> response= new ServiceResponse<>(false,null,"Maximum Otp attempt reached.");
        	return response;
        }
    	
    	if(temporaryUser.getExpirationTime()!=null && temporaryUser.getExpirationTime().isBefore(LocalDateTime.now())) {
    		ServiceResponse<User> response= new ServiceResponse<>(false,null,"Otp has expire");
    		return response;
    	}
    	//Find the details of user
    	
    	otpRepository.save(temporaryUser);
    	User user= userRepository.findByEmail(email);
    
    	ServiceResponse<User> response= new ServiceResponse<>(true,user,"Log in successful");
    	return response;
	}

}
