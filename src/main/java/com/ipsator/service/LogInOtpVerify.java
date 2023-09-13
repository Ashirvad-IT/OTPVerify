package com.ipsator.service;

import com.ipsator.payload.ServiceResponse;

public interface LogInOtpVerify {
	public ServiceResponse<Object> otpVerify(String email,String otp);
}
