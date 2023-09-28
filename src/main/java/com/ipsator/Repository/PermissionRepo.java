package com.ipsator.Repository;

import com.ipsator.Entity.Permission;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PermissionRepo extends JpaRepository<Permission, Integer>{
	public List<Permission> findByEmail(String email);
}
 