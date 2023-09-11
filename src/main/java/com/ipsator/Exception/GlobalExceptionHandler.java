package com.ipsator.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.ipsator.Entity.User;
/**
 * 
 * @author Ashirvad Kumar
 * This is gobalExceptionHandler class which is responsible for handling UserException, OneTimePasswordException
 *  if there will be other exception then Exception class will handle.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<MyErrorDetails> userExceptionHandler(UserException ue,WebRequest req){
		MyErrorDetails err=new MyErrorDetails(LocalDateTime.now(),ue.getMessage(),req.getDescription(false));
		return new ResponseEntity<MyErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(OneTimePasswordException.class)
	public ResponseEntity<MyErrorDetails> otpExceptionHandler(OneTimePasswordException otpe,WebRequest req){
		MyErrorDetails err=new MyErrorDetails(LocalDateTime.now(), otpe.getMessage(), req.getDescription(false));
		return new ResponseEntity<MyErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<MyErrorDetails> myExceptionHandler(Exception e, WebRequest req){
		MyErrorDetails err= new MyErrorDetails(LocalDateTime.now(), e.getMessage(), req.getDescription(false));
		return  new ResponseEntity<MyErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
}
