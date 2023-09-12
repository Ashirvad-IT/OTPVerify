package com.ipsator.controller;


import com.ipsator.Entity.User;
import com.ipsator.Record.UserDetails;
import com.ipsator.service.LoginService;
import com.ipsator.serviceImpl.LoginServiceImpl;

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

    private final LoginService otpLoginService;

    @Autowired
    public LoginController(LoginServiceImpl otpLoginService) {
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
    public ResponseEntity<String> userLogIn(@RequestParam String email) throws Exception {
        // Call the OTP login service to validate the OTP and perform login
        String loginResult = otpLoginService.userLogIn(email);

        // Return a success response
        
        return new ResponseEntity<String>(loginResult,HttpStatus.OK);
        
    }
}
