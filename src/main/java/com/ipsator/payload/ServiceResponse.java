package com.ipsator.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceResponse<T> {
	private boolean success;
	private T data;
	private String message;
	
	
	
}
