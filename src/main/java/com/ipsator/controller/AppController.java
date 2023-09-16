package com.ipsator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppController {
	
	
	@GetMapping("/sec")
	public ResponseEntity<String> secureEndpoint(){
		return new ResponseEntity<String>("It is secure end point",HttpStatus.OK);
	}
	
	
}
