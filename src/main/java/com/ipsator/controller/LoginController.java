package com.ipsator.controller;


import com.ipsator.Entity.Request;
import com.ipsator.Entity.Response;
import com.ipsator.Entity.User;
import com.ipsator.Record.OtpDetails;
import com.ipsator.Security.JwtHelper;
import com.ipsator.payload.ApiResponse;
import com.ipsator.payload.Error;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.LoginService;
import com.ipsator.serviceImpl.LoginServiceImpl;
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
public class LoginController {
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtHelper jwtHelper;
	
	
	private Logger logger= LoggerFactory.getLogger(Logger.class);	
	
	
	@GetMapping("/login")
	public ResponseEntity<Response> login(@RequestBody Request request){
		doAuthentication(request.getEmail(),request.getPassword());
		UserDetails user= userDetailsService.loadUserByUsername(request.getEmail());
		String token= jwtHelper.generateToken(user);
		
		Response jwtResponse= Response.builder()//
				.jwtToken(token)                //
				.username(user.getUsername()).build();
		return new ResponseEntity(jwtResponse,HttpStatus.OK);
	}

	private void doAuthentication(String email, String password) {
		UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(email, password);
		try {
			//Provider manager is the implementation of AuthenticationManager
			authenticationManager.authenticate(authenticationToken);
		}catch(BadCredentialsException exception){
			throw new BadCredentialsException("Invalid username or password");
		}
	}
	

	@ExceptionHandler(BadCredentialsException.class)
	public String exceptionHandler() {
		return "Credentials Invalid!!";
	}
}
