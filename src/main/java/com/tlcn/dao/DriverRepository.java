package com.tlcn.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tlcn.model.Driver;

public interface DriverRepository extends CrudRepository<Driver, String>{

	@Query("select d from driver d where d.sttdriver = 1")
	public List<Driver> getListDriverAvailable();
}
