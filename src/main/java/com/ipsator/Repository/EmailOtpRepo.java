package com.ipsator.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipsator.Entity.EmailOtp;

@Repository
public interface EmailOtpRepo extends JpaRepository<EmailOtp, Integer>{
	Optional<EmailOtp> findByEmail(String email);
	Optional<EmailOtp> findByOtp(String otp);
}
 