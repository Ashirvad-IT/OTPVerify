package com.ipsator.service;

import com.ipsator.Exception.OneTimePasswordException;
import com.ipsator.Record.OtpDetails;

public interface oneTimePasswordService {
	public OtpDetails generateOTP(String email) throws Exception;
}
