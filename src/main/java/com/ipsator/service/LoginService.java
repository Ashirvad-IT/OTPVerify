package com.ipsator.service;

import com.ipsator.Record.OtpDetails;
import com.ipsator.payload.ServiceResponse;

public interface LoginService {
	public ServiceResponse<OtpDetails> loginUser(String email);
}
 