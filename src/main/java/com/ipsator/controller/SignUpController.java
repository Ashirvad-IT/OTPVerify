package com.ipsator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipsator.Entity.User;
import com.ipsator.Record.OtpDetails;
import com.ipsator.payload.ApiResponse;
import com.ipsator.payload.Error;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.serviceImpl.SignUpServiceImpl;

import jakarta.validation.Valid;
/**
 * 
 * @author Ashirvad Kumar
 * @since 2023
 */
@RestController
@RequestMapping("/register")
public class SignUpController {
	private final SignUpServiceImpl registrationService;

    @Autowired
    public SignUpController(SignUpServiceImpl registrationService) {
        this.registrationService = registrationService;
    }
    /**
     * This method is calling registerUser method from RegistrationServiceImpl class
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping// user fix
    public ResponseEntity<ApiResponse> registerUser(@RequestBody @Valid User user) throws Exception {
    	ServiceResponse<OtpDetails> response= registrationService.registerUser(user);
    	if(response.isSuccess()) {
    		return new ResponseEntity<ApiResponse>(new ApiResponse("success", response.getData(), null),HttpStatus.OK);
    	}
    	else
    	return new ResponseEntity(new ApiResponse("Error",null,new Error(response.getMessage())),HttpStatus.BAD_REQUEST);
    }
}