package com.ipsator.service;

import com.ipsator.Exception.OneTimePasswordException;
import com.ipsator.Record.UserDetails;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain loginWithOtp method
 */
public interface LoginService {
	public String userLogIn(String email) throws Exception;
}
