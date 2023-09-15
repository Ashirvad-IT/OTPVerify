package com.ipsator.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This class is user entity
 * @author Ashirvad Kumar
 * 
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
	
	@Id
	private Long id;
	
	@Email
	@NotNull
	private String email;
	
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	private int age;
	
	private String gender;
	
	//created date
	//updated date
	
}
