package com.ipsator.Util;

import java.time.LocalDateTime;

import com.ipsator.Entity.User;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;

public class OtpEmailVerifier {
	UserRepo userRepo;
	public OtpEmailVerifier(UserRepo userRepo) {
		this.userRepo=userRepo;
	}
	
	public static void verifyEmail(String email) {
		
	}
}
