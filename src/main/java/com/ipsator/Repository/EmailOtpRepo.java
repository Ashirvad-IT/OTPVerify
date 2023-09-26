package com.ipsator.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsator.Entity.EmailOtp;

public interface EmailOtpRepo extends JpaRepository<EmailOtp, Integer>{
	Optional<EmailOtp> findByEmail(String email);
}
 