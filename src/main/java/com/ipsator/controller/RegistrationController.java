package com.ipsator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipsator.Entity.User;
import com.ipsator.serviceImpl.RegistrationServiceImpl;

import jakarta.validation.Valid;
/**
 * 
 * @author Ashirvad Kumar
 * @since 2023
 */
@RestController
@RequestMapping("/api/register")
public class RegistrationController {
	private final RegistrationServiceImpl registrationService;

    @Autowired
    public RegistrationController(RegistrationServiceImpl registrationService) {
        this.registrationService = registrationService;
    }
    /**
     * This method is calling registerUser method from RegistrationServiceImpl class
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody @Valid User user) throws Exception {
        return new ResponseEntity(registrationService.registerUser(user),HttpStatus.CREATED);
    }
}
