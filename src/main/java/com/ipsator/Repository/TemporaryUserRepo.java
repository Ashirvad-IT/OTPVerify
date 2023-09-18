package com.ipsator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsator.Entity.TemporaryUser;

public interface TemporaryUserRepo extends JpaRepository<TemporaryUser, Integer>{
	TemporaryUser findByEmail(String email); 
}
