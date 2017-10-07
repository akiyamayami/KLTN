package com.tlcn.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tlcn.dto.ModelCarReady;
import com.tlcn.model.Car;


public interface CarRepository extends CrudRepository<Car, Integer>{
	@Query("select c from car c where c.sttcar = 1 and c.driver != null and c.driver.sttdriver = 1")
	public List<Car> getListCarAvaliable();
	
	@Query("select c from car c where c.type = ?1 and c.sttcar != 3")
	public List<Car> getListFilter_Type(String type);
	
	@Query("select c from car c where c.seats = ?1 and c.sttcar != 3")
	public List<Car> getListFilter_Seat(int seats);
	
	@Query("select c from car c where c.type = ?1 and c.seats = ?2 and c.sttcar != 3")
	public List<Car> getListFilter_Type_Seat(String type, int seats);
	
	
	@Query("select c from car c where c.driver = null and c.sttcar != 3")
	public List<Car> getListCarFree();
}


