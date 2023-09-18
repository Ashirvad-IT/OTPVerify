package com.ipsator.service;

import com.ipsator.Entity.User;
import com.ipsator.Record.OtpDetails;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.payload.UserDto;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain registerUser method
 */
public interface SignUpService {
	public ServiceResponse<OtpDetails> registerUser(UserDto userDto) throws Exception;
}
