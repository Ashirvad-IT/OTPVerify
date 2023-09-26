package com.ipsator.controller;

import com.ipsator.payload.Error;
import com.ipsator.Entity.Request;
import com.ipsator.Entity.Response;
import com.ipsator.Record.OtpDetails;
import com.ipsator.Security.JwtHelper;
import com.ipsator.payload.ApiResponse;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LoginOtpVerifyService;
import com.ipsator.service.LoginService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@GetMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestParam String email){
		ServiceResponse<OtpDetails> result=  loginService.loginUser(email);
		if(result.isSuccess()) {
			return new ResponseEntity<ApiResponse>(new ApiResponse("Success",result.getData(), null),HttpStatus.OK);
		}
		return new ResponseEntity(new ApiResponse("Error",null,new Error(result.getMessage())),HttpStatus.BAD_REQUEST);
	}

}
