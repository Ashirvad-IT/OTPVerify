package com.ipsator.payload;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import com.ipsator.Entity.Permission;
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
    public List<String> permissions = new ArrayList<>(); 
   
}
