package com.ipsator.service;

import com.ipsator.Record.UserRecord;
import com.ipsator.payload.ServiceResponse;

public interface UserService {
	public ServiceResponse<UserRecord> createUser(UserRecord userRecord);
}
