package com.ipsator.service;

import com.ipsator.Entity.Response;
import com.ipsator.payload.ServiceResponse;

public interface LoginOtpVerifyService {
	public ServiceResponse<Response> verifyLogInOtp(String otp);
}
 