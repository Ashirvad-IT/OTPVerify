package com.ipsator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipsator.payload.ApiResponse;
import com.ipsator.payload.Error;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LogInOtpVerify;

@RestController
@RequestMapping("api/login")
public class LogInOtpVerifyController {
	private LogInOtpVerify logInOtpVerification;
	
	public LogInOtpVerifyController(LogInOtpVerify logInOtpVerification) {
		this.logInOtpVerification=logInOtpVerification;
	}
	@GetMapping("/otp")
	public ResponseEntity<ApiResponse> logInOtpVerify(@RequestParam String email,@RequestParam String otp){
		ServiceResponse<Object> response= logInOtpVerification.otpVerify(email, otp);
		if(response.isSuccess()) {
			return new ResponseEntity(new ApiResponse("success", response.getData(), null),HttpStatus.OK);
		}
		return new ResponseEntity(new ApiResponse("error",null,new Error(response.getMessage())),HttpStatus.BAD_REQUEST);
	}
}
