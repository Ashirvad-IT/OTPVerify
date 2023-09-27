package com.ipsator.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ipsator.Entity.Permission;
import com.ipsator.Entity.User;
import com.ipsator.Record.UserRecord;
import com.ipsator.Repository.PermissionRepo;
import com.ipsator.Repository.UserRepo;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.UserService;
@Service
public class UserServiceImpl implements UserService{
	
	private UserRepo userRepo;
	private PermissionRepo permissionRepo;
	
	@Autowired
	public UserServiceImpl(UserRepo userRepo,PermissionRepo permissionRepo) {
		this.userRepo=userRepo;
		this.permissionRepo=permissionRepo;
	}
	
	@Override
	public ServiceResponse<UserRecord> createUser(UserRecord userRecord) {
		if(userRecord==null) {
			return new ServiceResponse(false,null,"Please enter the details");
		}
		User user= userRepo.findByEmail(userRecord.email());
		if(user==null) {
			return new ServiceResponse<>(false,null,"User not log in");
		}
		
		user.setFirstName(userRecord.firstName());
    	user.setLastName(userRecord.lastName());
    	user.setEmail(userRecord.email());
    	user.setAge(userRecord.age());
    	user.setCreatedTime(LocalDateTime.now()); 
    	Set<Permission> allPermissions =new HashSet<>(); 
    	for(String el : userRecord.permissions()) {
    		Permission userPermission=new Permission();
    		userPermission.setName(el);
    		userPermission.setEmail(userRecord.email());
    		permissionRepo.save(userPermission);
    		allPermissions.add(userPermission);
    	}
    	user.setPermissions(allPermissions);
		userRepo.save(user);
		return new ServiceResponse<>(true,new UserRecord(userRecord.email(),userRecord.firstName(), userRecord.lastName(), userRecord.age(),null),"Profile got created");
	}

}
