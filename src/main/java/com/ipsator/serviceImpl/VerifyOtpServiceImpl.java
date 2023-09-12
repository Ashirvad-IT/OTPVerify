package com.ipsator.serviceImpl;

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
import com.ipsator.service.VerifyOtpService;
/**
 * 
 * @author Ashirvad Kumar
 * This class provide the implementation of OneTimePasswordService interface and provide the implementation to 
 * generateOtp method
 */
@Service
public class VerifyOtpServiceImpl implements VerifyOtpService {
	 private  OneTimePasswordRepository otpRepository;
	    private  JavaMailSender mailSender;
	    private UserRepo userRepo;

	    @Autowired
	    public VerifyOtpServiceImpl(OneTimePasswordRepository otpRepository, JavaMailSender mailSender,UserRepo userRepo) {
	        this.otpRepository = otpRepository;
	        this.mailSender = mailSender;
	        this.userRepo=userRepo;
	    }
    	/**
    	 * This method is responsible to generate otp it will take email and then generate otp 
    	 * 
    	 * @param email It is string data type
    	 * @return It will return OtpDetails record containing otp and expiry time of the otp 
    	 * @throws Exception
    	 */
	    public String verifyOTP(String email,String otp)throws Exception {
	    	
	    	OneTimePassword temporaryUser= otpRepository.findByEmail(email);
	    	
	    	/**
	    	 * Here user has not sign up and trying to generate otp
	    	 */
	    	if(temporaryUser==null) {
	    		throw new UserException("Please sign up first");
	    	}
	    	/**
	    	 * Here we are checking i.e user enter the otp before the expiry time
	    	 * otherwise he will get an exception
	    	 */
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
	    	if(temporaryUser.getExpirationTime()!=null && temporaryUser.getExpirationTime().isBefore(LocalDateTime.now())) {
	    		throw new OneTimePasswordException("OTP has expired.");
	    	}
	    	temporaryUser.setExpirationTime(null);
	    	temporaryUser.setLockoutEndTime(null);
	    	temporaryUser.setOtpAttempts(0);
	    	temporaryUser.setStatus("Active");
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
	        return "Sign up sucessfully";
	    }

	    
}
