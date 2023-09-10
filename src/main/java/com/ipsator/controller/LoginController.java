package com.ipsator.controller;


import com.ipsator.Entity.User;
import com.ipsator.Record.UserDetails;
import com.ipsator.serviceImpl.OtpLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final OtpLoginService otpLoginService;

    @Autowired
    public LoginController(OtpLoginService otpLoginService) {
        this.otpLoginService = otpLoginService;
    }

    @GetMapping("/otp")
    public ResponseEntity<UserDetails> loginWithOtp(@RequestParam String email, @RequestParam String otp) {
        // Call the OTP login service to validate the OTP and perform login
        UserDetails loginResult = otpLoginService.loginWithOtp(email, otp);

        // If login is successful, I will generate a JWT token here if needed

        // Return a success response
        
        return ResponseEntity.ok(loginResult);
        
    }
}
