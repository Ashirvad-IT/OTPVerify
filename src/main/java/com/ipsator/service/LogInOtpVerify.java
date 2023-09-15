package com.ipsator.service;

import com.ipsator.Entity.User;
import com.ipsator.payload.ServiceResponse;

public interface LogInOtpVerify {
	public ServiceResponse<User> otpVerify(String email,String otp);
}
