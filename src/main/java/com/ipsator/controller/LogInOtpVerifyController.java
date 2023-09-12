package com.ipsator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipsator.service.LogInOtpVerify;

@RestController
@RequestMapping("api/login")
public class LogInOtpVerifyController {
	private LogInOtpVerify logInOtpVerification;
	
	public LogInOtpVerifyController(LogInOtpVerify logInOtpVerification) {
		this.logInOtpVerification=logInOtpVerification;
	}
	@GetMapping("/otp")
	public ResponseEntity<String> logInOtpVerify(@RequestParam String email,@RequestParam String otp) throws Exception{
		return new ResponseEntity(logInOtpVerification.otpVerify(email, otp),HttpStatus.OK);
	}
}
