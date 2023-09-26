package com.ipsator.serviceImpl;
import java.security.AllPermission;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ipsator.Entity.*;
import com.ipsator.Entity.EmailOtp;
import com.ipsator.Entity.User;
import com.ipsator.Record.UserDetails;
import com.ipsator.Repository.EmailOtpRepo;
import com.ipsator.Repository.PermissionRepo;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.payload.UserDto;
import com.ipsator.service.SignUpOtpVerify;
/**
 * Service class responsible for verifying OTP (One-Time Password) during the signup process.
 * It performs OTP validation, account locking, and user creation.
 */
@Service
public class SignUpOtpVerifyImpl implements SignUpOtpVerify {
		
		@Value("${email.lockout.duration.hours}")
		private int emailLockoutDuration;
	 	private UserRepo userRepo;
	    private EmailOtpRepo emailOtpRepo;
	    private PermissionRepo permissionRepo;  
	    
	    @Autowired
	    public SignUpOtpVerifyImpl(UserRepo userRepo,EmailOtpRepo emailOtprepo,PermissionRepo permissionRepo) {	       
	        this.userRepo=userRepo;
	        this.emailOtpRepo=emailOtprepo;
	        this.permissionRepo=permissionRepo;
	    }
	    
	    public ServiceResponse<UserDetails> verifyOTP(UserDto userDto){	 
	    	User user= userRepo.findByEmail(userDto.getEmail());
	    	if(user!=null) {
	    		return new ServiceResponse<>(false,null,"User with this email already register");
	    	}
	    	Optional<EmailOtp> opt= emailOtpRepo.findByEmail(userDto.getEmail());
	    	if(opt.isEmpty()) {
	    		return new ServiceResponse<>(false,null,"Please sign up first");
	    	}
	    	 
	    	EmailOtp newUserOtpEmailDetails= opt.get();
	    	//Current Time
			LocalDateTime currentTime= LocalDateTime.now();
			// If OtpLockoutUntil is not null it means user is locked. There will be two possiblity
			// First condition can be user has overcome lockout time
			// Second condition can be user is trying to enter the otp within lockouttime			
			if(newUserOtpEmailDetails.getOtpLockoutUntil() != null){				
				if(newUserOtpEmailDetails.getOtpLockoutUntil().isAfter(currentTime)) {
					return new ServiceResponse<>(false,null,"User is lock unti "+newUserOtpEmailDetails.getOtpLockoutUntil());
				}else {
					newUserOtpEmailDetails.setOtpLockoutUntil(null);
					emailOtpRepo.save(newUserOtpEmailDetails);
				}
			}
			// If user otp attempts will greater then 5 then we will lock user for 3 hours.
			// we will set otp attempts = 0. when lockout time will complete, after i.e. we will allow user
			// to enter the otp.
			if(newUserOtpEmailDetails.getOtpAttempts() >= 5) {
				LocalDateTime lockOutTime = LocalDateTime.now().plusHours(emailLockoutDuration);
				newUserOtpEmailDetails.setOtpAttempts(0);
				newUserOtpEmailDetails.setOtpLockoutUntil(lockOutTime);
				emailOtpRepo.save(newUserOtpEmailDetails);
				return new ServiceResponse<>(false,null,"You have reach maximum attempts");
			}
			// If the user is entering wrong otp then we will increase otp attempts.
			if(!newUserOtpEmailDetails.getOtp().equals(userDto.getOtp())) {
				newUserOtpEmailDetails.setOtpAttempts(newUserOtpEmailDetails.getOtpAttempts()+1);
				emailOtpRepo.save(newUserOtpEmailDetails);
				return new ServiceResponse<>(false,null,"Please enter correst otp");
			}
			
			// Here we are checking otp is expire or not
			if(newUserOtpEmailDetails.getOtpExpireTime() != null && currentTime.isAfter(newUserOtpEmailDetails.getOtpExpireTime())) {
				return new ServiceResponse<>(false, null, "Otp is expired");
			}
			
	    	user=new User();
	    	user.setAge(userDto.getAge());
	    	user.setCreatedTime(LocalDateTime.now());
	    	user.setEmail(userDto.getEmail());
	    	user.setFirstName(userDto.getFirstName());
	    	user.setLastName(userDto.getLastName());
//	    	Permission userPsermission=new Permission();
	    	//S//et<String> permissions = new HashSet(); 
	    	List<Permission> allPermissions =new ArrayList(); 
	    	for(String el : userDto.getPermissions()) {
	    		Permission userPermission=new Permission();
	    		userPermission.setName(el);
	    		userPermission.setEmail(userDto.getEmail());
	    		permissionRepo.save(userPermission);
	    		allPermissions.add(userPermission);
	    	}
	    	user.setPermissions(allPermissions);
	    	newUserOtpEmailDetails.setEmailLockoutUntil(null);
	    	newUserOtpEmailDetails.setEmailsendAttempt(0);
			userRepo.save(user);
			emailOtpRepo.delete(newUserOtpEmailDetails);
			return new ServiceResponse<>(true, new UserDetails(user.getEmail(),user.getFirstName(),user.getLastName(),user.getAge()),null);
	    }

	    
}
