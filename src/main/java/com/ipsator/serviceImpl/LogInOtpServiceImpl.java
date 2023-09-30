package com.ipsator.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.ipsator.Entity.EmailOtp;
import com.ipsator.Entity.Response;
import com.ipsator.Entity.User;
import com.ipsator.Record.OtpDetails;
import com.ipsator.Repository.EmailOtpRepo;
import com.ipsator.Repository.UserRepo;
import com.ipsator.Security.JwtHelper;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LoginOtpVerifyService;


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
	public ServiceResponse<Response> verifyLogInOtp(OtpDetails otpDetails) {
		
		Optional<EmailOtp> opt= emailOtpRepo.findByEmail(otpDetails.email());
		if(opt.isEmpty()) {
			return new ServiceResponse<>(false,null,"Please generate otp first");
		}
		EmailOtp userEmailOtpDetails = opt.get();
		String email= userEmailOtpDetails.getEmail();
		User user=userRepo.findByEmail(email);	
		//If a new User will come then i will save his details
		if(user==null) {
			user=new User();
			user.setEmail(email);
			user.setCreatedTime(LocalDateTime.now());
			userRepo.save(user);
		}
		//Current Time
		LocalDateTime currentTime= LocalDateTime.now();
		//If OtpLockoutUntil is not null it means user is locked. There will be two possiblity
		// First condition can be user has overcome lockout time
		// Second condition can be user is trying to enter the otp within lockouttime
		if(userEmailOtpDetails.getOtpLockoutUntil() != null) {
			if(userEmailOtpDetails.getOtpLockoutUntil().isAfter(currentTime)) {
				return new ServiceResponse<>(false,null,"User is lock until "+ userEmailOtpDetails.getOtpLockoutUntil());
			}else {
				userEmailOtpDetails.setOtpLockoutUntil(null);
				emailOtpRepo.save(userEmailOtpDetails);
			}
		}
		// If user otp attempts will greater then 5 then we will lock user for 3 hours.
		// we will set otp attempts = 0. when lockout time will complete, after i.e. we will allow user
		// to enter the otp.
		if(userEmailOtpDetails.getOtpAttempts() >= 5) {
			LocalDateTime lockOutTime = LocalDateTime.now().plusHours(emailLockoutDuration);
			userEmailOtpDetails.setOtpAttempts(0);
			userEmailOtpDetails.setOtpLockoutUntil(lockOutTime);
			emailOtpRepo.save(userEmailOtpDetails);
			return new ServiceResponse<>(false,null,"You have reach maximum attempts");
		}
		// If the user is entering wrong otp then we will increase otp attempts.
		if(!userEmailOtpDetails.getOtp().equals(otpDetails.otp())) {
			userEmailOtpDetails.setOtpAttempts(userEmailOtpDetails.getOtpAttempts()+1);
			emailOtpRepo.save(userEmailOtpDetails);
			return new ServiceResponse<>(false,null,"Please enter correst otp");
		}		
		// Here we are checking otp is expire or not
		if(userEmailOtpDetails.getOtpExpireTime() != null && currentTime.isAfter(userEmailOtpDetails.getOtpExpireTime())) {
			return new ServiceResponse<>(false, null, "Otp is expired");
		}	
		
		//JWT token
		UserDetails userDetails= userDetailsService.loadUserByUsername(email);
		String token= jwtHelper.generateToken(userDetails);		
		Response jwtResponse= Response.builder()
				.jwtToken(token)                
				.username(userDetails.getUsername()).build();
		return new ServiceResponse<>(true,jwtResponse,"Login success");
	}
	
	

}
