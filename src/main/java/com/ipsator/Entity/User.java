package com.ipsator.Entity;

import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This class is user entity
 * @author Ashirvad Kumar
 * 
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private Long id;
	
	@Email
	@NotNull
	private String email;

	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	private int age;
	
	private String otp;
	
	private int emailsendAttempt;
	
	private LocalDateTime emailLockoutUntil; 
	
	private LocalDateTime lastEmailSend;
	
	private LocalDateTime createdTime;
	
	private LocalDateTime updatedDate;
	
	private LocalDateTime otpExpireTime;
	
	private LocalDateTime otpLockoutUntil;
	
	private int otpAttempts;
	
	private Boolean isUserVerified;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	

	
}
