package com.ipsator.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Record.OtpDetails;
import com.ipsator.service.SignUpOtpVerify;
import com.ipsator.serviceImpl.SignUpOtpVerifyImpl;
/**
 * 
 * @author Ashirvad Kumar
 * This class is oneTimePassword controller
 */
@RestController
@RequestMapping("/api/sinup")
public class SignUpOtpVerifyController {

    private final SignUpOtpVerify otpService;

    @Autowired
    public SignUpOtpVerifyController(SignUpOtpVerify otpService) {
        this.otpService = otpService;
    }
    /**
     * This method is calling generateOtp method which belong from OneTimePasswordServiceImpl class
     * @param email
     * @return
     * @throws Exception
     */
    @PostMapping("/otp")
    public ResponseEntity<String> generateOTP(@RequestParam String email, @RequestParam String otp) throws Exception {
        return new ResponseEntity(otpService.verifyOTP(email,otp),HttpStatus.OK);
    }
}
