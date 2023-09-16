package com.ipsator.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ipsator.Entity.User;
import com.ipsator.Repository.UserRepo;

@Component
public class CustomeUserDetailsService implements UserDetailsService{
	
	UserRepo userRepo;
	
	@Autowired
	public CustomeUserDetailsService(UserRepo userRepo) {
		this.userRepo=userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepo.findByEmail(username);
		if (user == null) {
	        throw new UsernameNotFoundException("User not found with username: " + username);
	    }
		return user;
	}
	
}
