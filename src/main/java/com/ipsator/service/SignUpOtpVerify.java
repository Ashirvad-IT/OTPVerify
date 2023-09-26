package com.ipsator.service;

import com.ipsator.Entity.User;
import com.ipsator.Record.UserRecord;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.payload.UserDto;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain generateOtp method
 */
public interface SignUpOtpVerify {
	public ServiceResponse<UserRecord> verifyOTP(UserDto userDto);
}
 