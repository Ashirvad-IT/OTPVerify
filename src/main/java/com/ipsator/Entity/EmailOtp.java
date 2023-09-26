package com.ipsator.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailOtp {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	 
	private String email;
	
	private String otp;
	
	private int emailsendAttempt;
	
	private LocalDateTime emailLockoutUntil; 
	
	private LocalDateTime lastEmailSend;

	private LocalDateTime otpExpireTime;
	
	private LocalDateTime otpLockoutUntil;
	
	private int otpAttempts;
}
