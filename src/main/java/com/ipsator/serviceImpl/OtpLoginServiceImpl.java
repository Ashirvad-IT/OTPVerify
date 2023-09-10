package com.ipsator.serviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.User;
import com.ipsator.Exception.OneTimePasswordException;
import com.ipsator.Exception.UserException;
import com.ipsator.Record.UserDetails;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.UserRepo;
import com.ipsator.service.otpLoginService;

import java.time.LocalDateTime;

@Service
public class OtpLoginServiceImpl implements otpLoginService{

    private final OneTimePasswordRepository otpRepository;
    private final UserRepo userRepository;

    @Autowired
    public OtpLoginServiceImpl(OneTimePasswordRepository otpRepository, UserRepo userRepository) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
    }

    public UserDetails loginWithOtp(String email, String otp) throws Exception {
    	//If user is already login
    	User user= userRepository.findByEmail(email);
    	if(user!=null) {
    		new UserException("user is already log in");
    	}
        //Retrieve the temporary user which is save in our oneTimePassword table 
        OneTimePassword temporaryUser = otpRepository.findByEmail(email);
        //If the user has sign up and not generated otp then we will throw Exception
        if(temporaryUser==null) {
        	throw new UserException("Please sign up first");
        }
        if(temporaryUser.getOtp()==null) {
        	throw new OneTimePasswordException("Please first generate otp");
        }
        if (temporaryUser == null || !temporaryUser.getOtp().equals(otp)) {
        	
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
        
        
        LocalDateTime currentTime = LocalDateTime.now();
        
        //Here we are checking our current otp is expire or not
        if (temporaryUser.getExpirationTime().isBefore(currentTime)) {       	
            throw new OneTimePasswordException("OTP has expired.");
        }
        
        //Here on successful login we will reset the otp and save the temporary user to our user table
        //and delete the temporary user from one time password table
        temporaryUser.setOtpAttempts(0);
        User newUser= new User();
        newUser.setAge(temporaryUser.getAge());
        newUser.setEmail(temporaryUser.getEmail());
        newUser.setFirstName(temporaryUser.getFirstName());
        newUser.setGender(temporaryUser.getGender());
        newUser.setLastName(temporaryUser.getLastName());
        otpRepository.delete(temporaryUser);
        
        UserDetails userDetails= new UserDetails(temporaryUser.getEmail(),temporaryUser.getFirstName(),temporaryUser.getLastName(),temporaryUser.getAge(),temporaryUser.getGender());;
        userRepository.save(newUser);
        return userDetails;
        
    }
}
