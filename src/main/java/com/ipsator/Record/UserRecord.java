package com.ipsator.Record;

import java.util.Set;

/**
 * 
 * @author Ashirvad Kumar
 * we are using UserDetails when we have to provide the response to user regarding User entity.
 */
public record UserRecord(String email,String firstName,String lastName,int age,Set<String> permissions) {

	public String email() {
		return email;
	}

	public String firstName() {
		return firstName;
	}

	public String lastName() {
		return lastName;
	}

	public int age() {
		return age;
	}

	public Set<String> permissions() {
		return permissions;
	}
	
}
 