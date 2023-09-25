package com.ipsator.service;

import com.ipsator.Entity.User;
import com.ipsator.Record.UserDetails;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.payload.UserDto;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain generateOtp method
 */
public interface SignUpOtpVerify {
	public ServiceResponse<UserDetails> verifyOTP(UserDto userDto);
}
 