package com.ipsator.controller;
import com.ipsator.payload.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ipsator.Entity.Response;
import com.ipsator.Record.OtpDetails;
import com.ipsator.payload.ApiResponse;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LoginOtpVerifyService;

@RestController
public class LoginOtpVerifyController {
	
	private LoginOtpVerifyService loginOtpVerifyService;
	
	public LoginOtpVerifyController(LoginOtpVerifyService loginOtpVerifyService) {
		this.loginOtpVerifyService=loginOtpVerifyService;
	}
	 
	@PostMapping("/login/otp")
	public ResponseEntity<ApiResponse> verifyLoginOtp(@RequestBody OtpDetails otpDetails){
		ServiceResponse<Response> result=loginOtpVerifyService.verifyLogInOtp(otpDetails);
		 if(result.isSuccess()) {
			 return new ResponseEntity<>(new ApiResponse("success",result.getData(),null),HttpStatus.OK);
		 }
		return new ResponseEntity<>(new ApiResponse("Error",null,new Error(result.getMessage())),HttpStatus.BAD_REQUEST);
	}
}
