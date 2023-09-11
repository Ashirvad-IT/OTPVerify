package com.ipsator.Exception;

import java.time.LocalDateTime;
/**
 * 
 * @author Ashirvad Kumar
 * MyErrorDetails is a user defined class which  contain attributes like timestamp, message, details.
 * The goal of this class is to provide the details of Exception in more structure way.
 */
public class MyErrorDetails {
	private LocalDateTime timestamp;
	private String message;
	private String details;
	
	public MyErrorDetails() {
		// TODO Auto-generated constructor stub
	}

	public MyErrorDetails(LocalDateTime timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
}
