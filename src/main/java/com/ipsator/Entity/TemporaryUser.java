package com.ipsator.Entity;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TemporaryUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Email
	@NotNull
	private String email;
	
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	private int age;
	
	private String gender;
	private LocalDateTime signUpTime;
	private int emailSentAttempts;
    private LocalDateTime lastEmailSendTime;
    private LocalDateTime emailLockUntil;
}
