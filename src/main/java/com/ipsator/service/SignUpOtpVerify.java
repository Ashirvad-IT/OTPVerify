package com.ipsator.service;

import com.ipsator.Exception.OneTimePasswordException;
import com.ipsator.Record.OtpDetails;
import com.ipsator.payload.ServiceResponse;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain generateOtp method
 */
public interface SignUpOtpVerify {
	public ServiceResponse<Object> verifyOTP(String email,String otp);
}
