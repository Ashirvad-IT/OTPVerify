package com.ipsator.service;

import com.ipsator.Exception.OneTimePasswordException;
import com.ipsator.Record.OtpDetails;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain generateOtp method
 */
public interface oneTimePasswordService {
	public OtpDetails generateOTP(String email) throws Exception;
}
