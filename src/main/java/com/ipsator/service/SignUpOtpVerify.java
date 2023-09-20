package com.ipsator.service;

import com.ipsator.Entity.User;
import com.ipsator.payload.ServiceResponse;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain generateOtp method
 */
public interface SignUpOtpVerify {
	public ServiceResponse<User> verifyOTP(String email,String otp);
}
