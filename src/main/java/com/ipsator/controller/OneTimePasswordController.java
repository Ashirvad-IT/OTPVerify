package com.ipsator.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Record.OtpDetails;
import com.ipsator.serviceImpl.OneTimePasswordServiceImpl;
/**
 * 
 * @author Ashirvad Kumar
 * This class is oneTimePassword controller
 */
@RestController
public class OneTimePasswordController {

    private final OneTimePasswordServiceImpl otpService;

    @Autowired
    public OneTimePasswordController(OneTimePasswordServiceImpl otpService) {
        this.otpService = otpService;
    }
    /**
     * This method is calling generateOtp method which belong from OneTimePasswordServiceImpl class
     * @param email
     * @return
     * @throws Exception
     */
    @PostMapping("/api/generate-otp")
    public ResponseEntity<OtpDetails> generateOTP(@RequestParam String email) throws Exception {
        return new ResponseEntity(otpService.generateOTP(email),HttpStatus.OK);
    }
}
