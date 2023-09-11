package com.ipsator.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsator.Entity.OneTimePassword;
/**
 * 
 * @author Ashirvad Kumar
 * This interface is responsible to make connection of the application with database i.e 
 * OneTimePasswordRepository table
 */
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long>{
	OneTimePassword findByEmail(String email);

}
