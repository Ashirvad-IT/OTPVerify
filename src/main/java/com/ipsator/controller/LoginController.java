package com.ipsator.controller;


import com.ipsator.Entity.User;
import com.ipsator.Record.UserDetails;
import com.ipsator.serviceImpl.OtpLoginServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * @author Ashirvad kumar
 * @since 2023
 * This class is Login controller, base url is api/login.
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final OtpLoginServiceImpl otpLoginService;

    @Autowired
    public LoginController(OtpLoginServiceImpl otpLoginService) {
        this.otpLoginService = otpLoginService;
    }
    /**
     * 
     * @param email
     * @param otp
     * @return
     * @throws Exception
     */
    @GetMapping()
    public ResponseEntity<UserDetails> loginWithOtp(@RequestParam String email, @RequestParam String otp) throws Exception {
        // Call the OTP login service to validate the OTP and perform login
        UserDetails loginResult = otpLoginService.loginWithOtp(email, otp);

        // If login is successful, I will generate a JWT token here if needed

        // Return a success response
        
        return new ResponseEntity<UserDetails>(loginResult,HttpStatus.OK);
        
    }
}
