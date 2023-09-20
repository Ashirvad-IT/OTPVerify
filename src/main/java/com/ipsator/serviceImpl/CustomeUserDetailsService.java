package com.ipsator.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ipsator.Entity.User;
import com.ipsator.Repository.UserRepo;
/**
 * Custom implementation of Spring Security's UserDetailsService interface.
 * This class is responsible for loading user details by username/email
 * and converting them into a UserDetails object.
 * 
 * @see org.springframework.security.core.userdetails.UserDetailsService
 */
@Component
public class CustomeUserDetailsService implements UserDetailsService{
	
	UserRepo userRepo;
	
	 /**
     * Constructs a new CustomeUserDetailsService with the specified UserRepo dependency.
     * 
     * @param userRepo The UserRepo repository to retrieve user details from.
     */
	
	@Autowired
	public CustomeUserDetailsService(UserRepo userRepo) {
		this.userRepo=userRepo;
	}
	
	/**
     * Load user details by username (email). This method is called by Spring Security
     * when attempting to authenticate a user.
     * 
     * @param username The username (email) of the user.
     * @return A UserDetails object representing the authenticated user.
     * @throws UsernameNotFoundException If the user with the given username is not found.
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepo.findByEmail(username);
		if (user == null) {
	        throw new UsernameNotFoundException("User not found with username: " + username);
	    }
		return user;
	}
	
}
