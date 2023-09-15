package com.ipsator.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This is OneTimePassword entity class 
 * @author Ashirvad Kumar
 *
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OneTimePassword {

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
    
}
