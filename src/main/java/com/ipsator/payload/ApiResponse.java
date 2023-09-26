package com.ipsator.payload;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
	private String status;
	private Object data;
	private Error error;
	
	 
}
