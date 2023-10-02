package com.ipsator.controller;

import com.ipsator.payload.Error;
import com.ipsator.Record.OtpDetails;
import com.ipsator.payload.ApiResponse;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * @author Ashirvad kumar
 * @since 2023
 * This class is Login controller, base url is api/login.
 */
@RestController
public class LoginController {
	 
	private LoginService loginService;
	
	public LoginController(LoginService loginService) {
		this.loginService=loginService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestParam String email){
		ServiceResponse<OtpDetails> result=  loginService.loginUser(email);
		return result.getResponse();
	}

}
