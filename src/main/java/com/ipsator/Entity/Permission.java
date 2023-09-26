package com.ipsator.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Permission {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	 
	private String email;

    @NotNull
    private String name;
}
