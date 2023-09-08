package com.ipsator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsator.Entity.User;

public interface UserRepo extends JpaRepository<User, Long>{
	User findByEmail(String email);
}
