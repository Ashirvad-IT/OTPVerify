package com.ipsator.Record;
/**
 * 
 * @author Ashirvad Kumar
 * we are using UserDetails when we have to provide the response to user regarding User entity.
 */
public record UserDetails(String email,String firstName,String lastName,int age) {
	
}
 