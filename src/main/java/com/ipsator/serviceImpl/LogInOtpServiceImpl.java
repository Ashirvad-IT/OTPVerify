package com.ipsator.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.EmailOtp;
import com.ipsator.Entity.Request;
import com.ipsator.Entity.Response;
import com.ipsator.Entity.User;
import com.ipsator.Repository.EmailOtpRepo;
import com.ipsator.Repository.UserRepo;
import com.ipsator.Security.JwtHelper;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LoginOtpVerifyService;
import com.ipsator.service.LoginService;

@Service
public class LogInOtpServiceImpl implements LoginOtpVerifyService{
	
	@Value("${email.lockout.duration.hours}")
	private int emailLockoutDuration;
	private UserRepo userRepo;
	private JwtHelper jwtHelper;
	private EmailOtpRepo emailOtpRepo;
	private UserDetailsService userDetailsService;
	@Autowired
	public LogInOtpServiceImpl(UserRepo userRepo,JwtHelper jwtHelper,EmailOtpRepo emailOtpRepo,UserDetailsService userDetailsService) {
		// TODO Auto-generated constructor stub
		this.userRepo=userRepo;
		this.jwtHelper=jwtHelper;
		this.emailOtpRepo=emailOtpRepo;
		this.userDetailsService=userDetailsService;
	}
	//otp-> otpExpireTime, otpLockoutTime, otpAttempts
	@Override
	public ServiceResponse<Response> verifyLogInOtp(Request request) {
		
		Optional<EmailOtp> opt= emailOtpRepo.findByEmail(request.getEmail()); 
		if(opt.isEmpty()) {
			return new ServiceResponse<>(false,null,"Please Generate OTP first");

		}
		EmailOtp newUserOtpEmailDetails= opt.get();
		//Current Time
		LocalDateTime currentTime= LocalDateTime.now();
		// If OtpLockoutUntil is not null it means user is locked. There will be two possiblity
		// First condition can be user has overcome lockout time
		// Second condition can be user is trying to enter the otp within lockouttime
		if(newUserOtpEmailDetails.getOtpLockoutUntil() != null) {
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
		if(!newUserOtpEmailDetails.getOtp().equals(request.getOtp())) {
			newUserOtpEmailDetails.setOtpAttempts(newUserOtpEmailDetails.getOtpAttempts()+1);
			emailOtpRepo.save(newUserOtpEmailDetails);
			return new ServiceResponse<>(false,null,"Please enter correst otp");
		}
		
		// Here we are checking otp is expire or not
		if(newUserOtpEmailDetails.getOtpExpireTime() != null && currentTime.isAfter(newUserOtpEmailDetails.getOtpExpireTime())) {
			return new ServiceResponse<>(false, null, "Otp is expired");
		}
		
		//JWT token
		UserDetails userDetails= userDetailsService.loadUserByUsername(request.getEmail());
		String token= jwtHelper.generateToken(userDetails);
		
		Response jwtResponse= Response.builder()//
				.jwtToken(token)                //
				.username(userDetails.getUsername()).build();
		return new ServiceResponse<>(true,jwtResponse,"Login success");
	}
	
	

}
