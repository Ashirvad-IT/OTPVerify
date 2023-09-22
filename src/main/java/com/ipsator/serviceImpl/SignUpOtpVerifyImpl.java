package com.ipsator.serviceImpl;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.User;
import com.ipsator.Record.UserDetails;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.SignUpOtpVerify;
/**
 * Service class responsible for verifying OTP (One-Time Password) during the signup process.
 * It performs OTP validation, account locking, and user creation.
 */
@Service
public class SignUpOtpVerifyImpl implements SignUpOtpVerify {
	
	 	
	 	private UserRepo userRepo;
	    
	    
	    @Autowired
	    public SignUpOtpVerifyImpl(UserRepo userRepo) {	       
	        this.userRepo=userRepo;
	    }
	    
	    public ServiceResponse<UserDetails> verifyOTP(String email,String otp){	    	
	    	User user= userRepo.findByEmail(email);
	    	if(user==null) {
	    		return new ServiceResponse<>(false,null,"Please sign up first");
	    	}
	    	//Current Time
			LocalDateTime currentTime= LocalDateTime.now();
			// If OtpLockoutUntil is not null it means user is locked. There will be two possiblity
			// First condition can be user has overcome lockout time
			// Second condition can be user is trying to enter the otp within lockouttime
			if(user.getOtpLockoutUntil() != null) {				
				if(user.getOtpLockoutUntil().isAfter(currentTime)) {
					return new ServiceResponse<>(false,null,"User is lock unti "+user.getOtpLockoutUntil());
				}else {
					user.setOtpLockoutUntil(null);
				}
			}
			// If the user is entering wrong otp then we will increase otp attempts.
			if(!user.getOtp().equalsIgnoreCase(otp)) {
				user.setOtpAttempts(user.getOtpAttempts()+1);
			}
			// If user otp attempts will greater then 5 then we will lock user for 3 hours.
			// we will set otp attempts = 0. when lockout time will complete, after i.e. we will allow user
			// to enter the otp.
			if(user.getOtpAttempts() > 5) {
				LocalDateTime lockOutTime = user.getOtpLockoutUntil().plusHours(3);
				user.setOtpAttempts(0);
			}
			// Here we are checking otp is expire or not
			if(user.getOtpExpireTime() != null && currentTime.isAfter(user.getOtpExpireTime())) {
				return new ServiceResponse<>(false, null, "Otp is expired");
			}
	    	
			user.setIsUserVerified(true);
			userRepo.save(user);
			return new ServiceResponse<>(true, new UserDetails(email,user.getFirstName(),user.getLastName(),user.getAge()),null);
	    }

	    
}
