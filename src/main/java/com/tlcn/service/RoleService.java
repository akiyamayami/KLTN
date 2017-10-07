package com.tlcn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlcn.dao.RoleRepository;
import com.tlcn.model.Role;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;
	
	public List<Role> findAll(){
		List<Role> roles = new ArrayList<>();
		for(Role role : roleRepository.findAll()){
			roles.add(role);
		}
		return roles;
	}
	
	public Role findOne(int roleID){
		return roleRepository.findOne(roleID);
	}
}
