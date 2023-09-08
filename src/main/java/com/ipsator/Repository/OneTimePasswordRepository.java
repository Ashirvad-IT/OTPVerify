package com.ipsator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsator.Entity.OneTimePassword;

public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long>{
	OneTimePassword findByEmail(String email);

}
