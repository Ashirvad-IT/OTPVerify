package com.ipsator.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.User;
import com.ipsator.Repository.OneTimePasswordRepository;
import com.ipsator.Repository.UserRepo;

import java.time.LocalDateTime;

@Service
public class OtpLoginService {

    private final OneTimePasswordRepository otpRepository;
    private final UserRepo userRepository;

    @Autowired
    public OtpLoginService(OneTimePasswordRepository otpRepository, UserRepo userRepository) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
    }

    public User loginWithOtp(String email, String otp) {
        // Retrieve the OTP record from the database
        OneTimePassword otpRecord = otpRepository.findByEmail(email);

        if (otpRecord == null || !otpRecord.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        //Here we are checking our current otp is expire or not
        if (otpRecord.getExpirationTime().isBefore(currentTime)) {
            throw new RuntimeException("OTP has expired");
        }

        //The purpose of deleting the OTP (One-Time Password) record in the OtpLoginService class is to ensure
        //that each OTP can only be used once for authentication. Once an OTP has been successfully used for login,
        //it should be invalidated to prevent its reuse.
        otpRepository.delete(otpRecord);

        // Return a success message
        return userRepository.findByEmail(email);
        
    }
}
