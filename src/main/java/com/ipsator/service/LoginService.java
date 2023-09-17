package com.ipsator.service;


import com.ipsator.Record.OtpDetails;
import com.ipsator.Record.UserDetails;
import com.ipsator.payload.ServiceResponse;
/**
 * 
 * @author Ashirvad Kumar
 * This interface contain loginWithOtp method
 */
public interface LoginService {
	public ServiceResponse<OtpDetails> userLogIn(String email);
}
