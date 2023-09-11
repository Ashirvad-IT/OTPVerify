package com.ipsator.Exception;
/**
 * 
 * @author Ashirvad Kumar
 *	It is a user define OneTimePasswordException and it will throw Checked Exception
 * if there will be any exception related with OneTimePassword entity.
 */
public class OneTimePasswordException extends Exception{
	public OneTimePasswordException() {
		// TODO Auto-generated constructor stub
	}
	public OneTimePasswordException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}
}
