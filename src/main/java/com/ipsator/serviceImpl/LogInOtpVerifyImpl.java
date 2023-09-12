package com.ipsator.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.User;
import com.ipsator.Exception.OneTimePasswordException;
import com.ipsator.Exception.UserException;
import com.ipsator.Record.UserDetails;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.UserRepo;
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
	public String otpVerify(String email, String otp) throws Exception{
		
		OneTimePassword temporaryUser= otpRepository.findByEmail(email);
		
		if(temporaryUser==null) {
    		throw new UserException("Please sign up first");
    	}
		if(temporaryUser!=null && temporaryUser.getLockoutEndTime() != null) {
    		LocalDateTime currentTime= LocalDateTime.now();
    		LocalDateTime lockOutEndTime= temporaryUser.getLockoutEndTime();
    		if(currentTime.isBefore(lockOutEndTime)) {
    			throw new OneTimePasswordException("Please try after "+temporaryUser.getLockoutEndTime());
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
            	throw new OneTimePasswordException("Invalid otp");
            }
        }
    	
    	//If the user has reached or exceed its otp attempts 
        if(temporaryUser.getOtpAttempts()>=5) {
        	//Lock the user for 3 hours because it has reach its maximum attempts
        	LocalDateTime lockOutEndTIme=LocalDateTime.now().plusHours(3);
        	temporaryUser.setLockoutEndTime(lockOutEndTIme);
//        	temporaryUser.setOtpAttempts(0);
        	otpRepository.save(temporaryUser);
        	throw new OneTimePasswordException("Maximum otp attempts reached.");
        }
    	
    	if(temporaryUser.getExpirationTime()!=null && temporaryUser.getExpirationTime().isBefore(LocalDateTime.now())) {
    		throw new OneTimePasswordException("OTP has expired.");
    	}
    	//Find the details of user
    	User user= userRepository.findByEmail(email);
    	UserDetails userDetails= new UserDetails(user.getEmail(), user.getFirstName(), user.getLastName(), user.getAge(), user.getGender());
		return userDetails.toString();
	}

}
