package com.ipsator.service;

import com.ipsator.Exception.OneTimePasswordException;
import com.ipsator.Record.OtpDetails;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain generateOtp method
 */
public interface SignUpOtpVerify {
	public String verifyOTP(String email,String otp) throws Exception;
}
