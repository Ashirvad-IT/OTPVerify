package com.ipsator.serviceImpl;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.TemporaryUser;
import com.ipsator.Entity.User;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.TemporaryUserRepo;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.SignUpOtpVerify;
/**
 * Service class responsible for verifying OTP (One-Time Password) during the signup process.
 * It performs OTP validation, account locking, and user creation.
 */
@Service
public class SignUpOtpVerifyImpl implements SignUpOtpVerify {
	
	 	private  OneTimePasswordRepository otpRepository;
	 	private UserRepo userRepo;
	    private PasswordEncoder passwordEncoder;
	    private TemporaryUserRepo temporaryUserRepo;
	    /**
	     * Constructs a new SignUpOtpVerifyImpl with the required dependencies.
	     * 
	     * @param otpRepository      The repository for managing OTP information.
	     * @param userRepo           The repository for managing user information.
	     * @param passwordEncoder    The password encoder for hashing OTPs.
	     * @param temporaryUserRepo The repository for temporary user information.
	     */
	    @Autowired
	    public SignUpOtpVerifyImpl(OneTimePasswordRepository otpRepository, UserRepo userRepo,PasswordEncoder passwordEncoder,TemporaryUserRepo temporaryUserRepo) {
	        this.otpRepository = otpRepository;
	        this.userRepo=userRepo;
	        this.passwordEncoder=passwordEncoder;
	        this.temporaryUserRepo=temporaryUserRepo;
	    }
	    /**
	     * Verify the OTP for a given email during the signup process.
	     * 
	     * @param email The email associated with the OTP.
	     * @param otp   The OTP to be verified.
	     * @return A ServiceResponse with information about the verification result.
	     */
	    public ServiceResponse<User> verifyOTP(String email,String otp){
	    	
	    	OneTimePassword otpInformation= otpRepository.findByEmail(email);
	    	
	    	
	    	if(otpInformation==null) {
	    		return new ServiceResponse(false,null,"Please sign up first");
	    	}
	    	
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
