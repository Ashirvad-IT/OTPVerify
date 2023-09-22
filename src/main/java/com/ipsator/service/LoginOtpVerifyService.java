package com.ipsator.service;

import com.ipsator.Entity.Request;
import com.ipsator.Entity.Response;
import com.ipsator.Record.UserDetails;
import com.ipsator.payload.ServiceResponse;

public interface LoginOtpVerifyService {
	public ServiceResponse<Response> verifyLogInOtp(Request request);
}
