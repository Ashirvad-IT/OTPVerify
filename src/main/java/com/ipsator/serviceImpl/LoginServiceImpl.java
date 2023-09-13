package com.ipsator.serviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.User;
import com.ipsator.Exception.OneTimePasswordException;
import com.ipsator.Exception.UserException;
import com.ipsator.Record.OtpDetails;
import com.ipsator.Record.UserDetails;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LoginService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class LoginServiceImpl implements LoginService{

    private final OneTimePasswordRepository otpRepository;
    private final UserRepo userRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public LoginServiceImpl(OneTimePasswordRepository otpRepository, UserRepo userRepository,JavaMailSender mailSender) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.mailSender=mailSender;
    }
    /**
     * 
     */
    public ServiceResponse<Object> userLogIn(String email) {
    	//If user is already login
    	User user= userRepository.findByEmail(email);
    	if(user!=null) {
    		return new ServiceResponse<Object>(false, null, "User is already log in");
    	}
        //Retrieve the temporary user which is save in our oneTimePassword table 
        OneTimePassword temporaryUser = otpRepository.findByEmail(email);
        //If the user has sign up and not generated otp then we will throw Exception
        if(temporaryUser==null) {
        	return new ServiceResponse<Object>(false, null, "Please sign up first");
        }
        
        // Generate a random 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(1000000));

        // Set OTP expiration time (e.g., 5 minutes from now)
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        
        temporaryUser.setOtp(otp);
        temporaryUser.setExpirationTime(expirationTime);
        
        otpRepository.save(temporaryUser);
        
        OtpDetails otpDetails= new OtpDetails(user.getEmail(),otp,expirationTime);
        
        sendOtpByEmail(email, otp);
        Map<String,String> data= new HashMap();
        data.put("Result", otpDetails.toString());
        return new ServiceResponse<Object>(true, data, "Otp send on your email");
        
    }
    
    private void sendOtpByEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        mailSender.send(message);
    }
}
