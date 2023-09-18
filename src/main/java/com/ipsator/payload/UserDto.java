package com.ipsator.payload;

import java.time.LocalDateTime;

import com.ipsator.Entity.OneTimePassword;
import com.ipsator.Entity.TemporaryUser;
import com.ipsator.Entity.User;

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
public class UserDto {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private int age;
    private String otp;    
    private LocalDateTime expirationTime;
    private int otpAttempts;
    private LocalDateTime lockoutEndTime;
    private LocalDateTime signUpTime;
    private int  emailSendAttempts;
    private LocalDateTime lastEmailsendtime;
    
    public User mapToUser(UserDto userDto) {
    	User user= new User();
    	user.setEmail(userDto.getEmail());
    	user.setAge(userDto.getAge());
    	user.setFirstName(userDto.getFirstName());
    	user.setGender(userDto.getGender());
    	user.setLastName(userDto.getLastName());
    	user.setPassword(userDto.getOtp());
    	return user;
    }
    
    
    
    public OneTimePassword mapToOneTimePassword(UserDto userDto) {
    	OneTimePassword oneTimePassword= new OneTimePassword();
    	oneTimePassword.setOtp(userDto.getOtp());
    	oneTimePassword.setExpirationTime(userDto.getExpirationTime());
    	oneTimePassword.setEmail(userDto.getEmail());
    	oneTimePassword.setLockoutEndTime(userDto.getLockoutEndTime());
    	oneTimePassword.setOtpAttempts(userDto.getOtpAttempts());
    	return oneTimePassword;
    }
}