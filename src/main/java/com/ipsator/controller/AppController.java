package com.ipsator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ipsator.Record.UserRecord;
import com.ipsator.payload.ApiResponse;
import com.ipsator.payload.Error;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api")
public class AppController {
	
	private final UserService userService;
	
	@Autowired
	public AppController(UserService userService) {
		this.userService=userService;
	}
	
	@PostMapping("/user")
	public ResponseEntity<ApiResponse> createUser(@RequestBody UserRecord userRecord){
		if(userRecord==null) {
			//new ResponseEntity(new ApiResponse("Error",null,new Error(result.getMessage())),HttpStatus.BAD_REQUEST)
			return new ResponseEntity<>(new ApiResponse("Error",null,new Error("Please enter the details")),HttpStatus.BAD_REQUEST);
		}
		ServiceResponse<UserRecord> result  = userService.createUser(userRecord);
		return result.getResponse();
	}
	
	@GetMapping()
	@PreAuthorize("hasAuthority('User:Read')")
	public ResponseEntity<String> secureEndpoint(){
		return new ResponseEntity<String>("It is secure end point",HttpStatus.OK);
	}
	 
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('User:Write')")
	public ResponseEntity<String> createResource(){
		return new ResponseEntity<String>("Resource got created",HttpStatus.CREATED);
	}
	
	@DeleteMapping()
	@PreAuthorize("hasAuthority('User:Delete')")
	public ResponseEntity<String> deleteResource(){
		return new ResponseEntity<String>("Resource got deleted",HttpStatus.OK);
	}
	
	@PutMapping()
	@PreAuthorize("hasAuthority('User:Update')")
	public ResponseEntity<String> updateResource(){
		return new ResponseEntity<String>("Resource got updated",HttpStatus.OK);
	}
}
