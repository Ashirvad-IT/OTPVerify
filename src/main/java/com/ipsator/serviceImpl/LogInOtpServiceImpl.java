package com.ipsator.serviceImpl;

import java.time.LocalDateTime;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.Request;
import com.ipsator.Entity.Response;
import com.ipsator.Entity.User;
import com.ipsator.Repository.UserRepo;
import com.ipsator.Security.JwtHelper;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LoginOtpVerifyService;
import com.ipsator.service.LoginService;

@Service
public class LogInOtpServiceImpl implements LoginOtpVerifyService{
	
	@Value("${email.lockout.duration.hours}")
	private int emailLockoutDuration;
	private UserDetailsService userDetailsService;
	private UserRepo userRepo;
	private JwtHelper jwtHelper;
	@Autowired
	public LogInOtpServiceImpl(UserRepo userRepo,UserDetailsService userDetailsService, JwtHelper jwtHelper) {
		// TODO Auto-generated constructor stub
		this.userRepo=userRepo;
		this.userDetailsService=userDetailsService;
		this.jwtHelper=jwtHelper;
	}
	//otp-> otpExpireTime, otpLockoutTime, otpAttempts
	@Override
	public ServiceResponse<Response> verifyLogInOtp(Request request) {
		// Extract details of user from Database
		User user= userRepo.findByEmail(request.getEmail());
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
		if(!user.getOtp().equalsIgnoreCase(request.getOtp())) {
			user.setOtpAttempts(user.getOtpAttempts()+1);
		}
		// If user otp attempts will greater then 5 then we will lock user for 3 hours.
		// we will set otp attempts = 0. when lockout time will complete, after i.e. we will allow user
		// to enter the otp.
		if(user.getOtpAttempts() > 5) {
			LocalDateTime lockOutTime = user.getOtpLockoutUntil().plusHours(emailLockoutDuration);
			user.setOtpAttempts(0);
		}
		// Here we are checking otp is expire or not
		if(user.getOtpExpireTime() != null && currentTime.isAfter(user.getOtpExpireTime())) {
			return new ServiceResponse<>(false, null, "Otp is expired");
		}
		UserDetails userDetails= userDetailsService.loadUserByUsername(request.getEmail());
		String token= jwtHelper.generateToken(user);
		
		Response jwtResponse= Response.builder()//
				.jwtToken(token)                //
				.username(user.getUsername()).build();
		return new ServiceResponse<>(true,jwtResponse,"Login success");
	}
	
	

}
