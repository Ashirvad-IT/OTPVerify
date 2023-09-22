package com.ipsator.service;

import com.ipsator.Entity.User;
import com.ipsator.Record.UserDetails;
import com.ipsator.payload.ServiceResponse;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain generateOtp method
 */
public interface SignUpOtpVerify {
	public ServiceResponse<UserDetails> verifyOTP(String email,String otp);
}
