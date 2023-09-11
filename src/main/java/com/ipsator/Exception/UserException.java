package com.ipsator.Exception;
/**
 * 
 * @author Ashirvad Kumar
 * This class is a user define exception class and it will throw checked Exception   
 * if there will be any exception related to user entity.
 */
public class UserException extends Exception{
	public UserException() {
		// TODO Auto-generated constructor stub
	}
	public UserException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}
}
