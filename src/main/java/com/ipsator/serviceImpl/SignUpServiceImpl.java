package com.ipsator.serviceImpl;

import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.TemporaryUser;
import com.ipsator.Record.OtpDetails;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.TemporaryUserRepo;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.payload.UserDto;
import com.ipsator.service.SignUpService;
/**
 * 
 * @author Ashirvad Kumar
 * @since 2023
 * This is RegisterService class, here we are saving the details of user in a temporary table
 * i.e. OneTimePassword table. Because there may be a condition where user have sign up and after
 * that he will not able to enter the right otp.
 */
@Service
public class SignUpServiceImpl implements SignUpService {
	
    private final UserRepo userRepository;
    private  JavaMailSender mailSender;
    private final OneTimePasswordRepository otpRepository;
    private final TemporaryUserRepo temporaryUserRepo;
    

    @Autowired
    public SignUpServiceImpl(UserRepo userRepository, OneTimePasswordRepository otpRepository,JavaMailSender mailSender,TemporaryUserRepo temporaryUserRepo) {
        this.userRepository = userRepository;
        this.otpRepository=otpRepository;
        this.mailSender=mailSender;
        this.temporaryUserRepo=temporaryUserRepo;
    }
    
    /**
     *Here we are first checking user with this email is already exist or not.If user with this id is already
     *there then we will throw an Exception. If it is a new User then we will save the details.
     *@param user This will be the object of user class containing the details of user like first name,last name,
     *email etc.
     *@return It will return a string if the user has successfully register
     *@throws
     */

    public ServiceResponse<OtpDetails> registerUser(UserDto userDto) throws Exception{
        // Check if the email is already registered
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
        	return new ServiceResponse<>(false,null,"Email already registered");
            
        }
        TemporaryUser temporaryUser= temporaryUserRepo.findByEmail(userDto.getEmail());
        if(temporaryUser!=null) {
        	int emailSendAttempts = temporaryUser.getEmailSentAttempts();
        	if(emailSendAttempts >= 3) {
        		LocalDateTime now=LocalDateTime.now();
        		if(now.isBefore(temporaryUser.getEmailLockUntil())) {
        			return new ServiceResponse<>(false,null,"Account is locked till "+temporaryUser.getEmailLockUntil());
        		}else {
        			emailSendAttempts=1;
        			temporaryUser.setEmailLockUntil(null);
        			temporaryUser.setEmailSentAttempts(emailSendAttempts);
        		} 
        	}
        	else {
        		emailSendAttempts= emailSendAttempts+1;
        		temporaryUser.setEmailSentAttempts(emailSendAttempts);
        	}
        }
        if(temporaryUser != null && temporaryUser.getEmailSentAttempts()>=3) {
        	temporaryUser.setEmailLockUntil(LocalDateTime.now().plusHours(3));
        }
        // Generate a random 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(1000000));

        // Set OTP expiration time (e.g., 5 minutes from now)
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        
        // Send the OTP via email
        sendOtpByEmail(userDto.getEmail(), otp);
        
        //Saving otp related information in otp table
        OneTimePassword otpInformation= otpRepository.findByEmail(userDto.getEmail());
        if(otpInformation==null) {
        	otpInformation=new OneTimePassword();
            otpInformation.setEmail(userDto.getEmail());
            otpInformation.setOtp(otp);
            otpInformation.setExpirationTime(expirationTime);
        }else {
        	otpInformation.setOtp(otp);
            otpInformation.setExpirationTime(expirationTime);
        }
        otpRepository.save(otpInformation);
        
        if(temporaryUser==null) {
        	temporaryUser= new TemporaryUser();
        	temporaryUser.setEmailSentAttempts(1);
        }
        
        //Until the email is not verify user details will save in temporaryUser table
        temporaryUser.setFirstName(userDto.getFirstName());
        temporaryUser.setLastName(userDto.getLastName());
        temporaryUser.setEmail(userDto.getEmail());
        temporaryUser.setAge(userDto.getAge());
        temporaryUser.setGender(userDto.getGender());
        temporaryUser.setLastEmailSendTime(LocalDateTime.now());
        temporaryUserRepo.save(temporaryUser);
        OtpDetails otpDetails= new OtpDetails(userDto.getEmail(),otp,expirationTime);
        return new ServiceResponse(true,otpDetails,"Verify your email");
        
    }
    private void sendOtpByEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        mailSender.send(message);
    }
    
    
    
}
