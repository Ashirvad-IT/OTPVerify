package com.ipsator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipsator.Entity.User;
/**
 * 
 * @author Ashirvad Kumar
 * This interface is responsible to make connection of the application with database i.e 
 * User table
 *
 */
@Repository 
public interface UserRepo extends JpaRepository<User, Long>{
	User findByEmail(String email);
}
 