package com.tlcn.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tlcn.model.User;

public interface UserRespository extends CrudRepository<User, String> {
	@Query("select u from user u where u.roleUser.roleID != 1")
	public List<User> getListBGMAndPTBVT();
}
