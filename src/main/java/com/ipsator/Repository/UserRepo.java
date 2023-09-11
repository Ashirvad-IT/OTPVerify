package com.ipsator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsator.Entity.User;
/**
 * 
 * @author Ashirvad Kumar
 * This interface is responsible to make connection of the application with database i.e 
 * User table
 *
 */
public interface UserRepo extends JpaRepository<User, Long>{
	User findByEmail(String email);
}
