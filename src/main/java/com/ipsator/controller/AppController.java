package com.ipsator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppController {
	
	
	@GetMapping("/sec")
	@PreAuthorize("hasAuthority('User:Read')")
	public ResponseEntity<String> secureEndpoint(){
		return new ResponseEntity<String>("It is secure end point",HttpStatus.OK);
	}
	 
	@PostMapping()
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
