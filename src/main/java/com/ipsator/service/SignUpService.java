package com.ipsator.service;

import com.ipsator.Entity.User;
import com.ipsator.payload.ServiceResponse;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain registerUser method
 */
public interface SignUpService {
	public ServiceResponse<Object> registerUser(User user) throws Exception;
}
