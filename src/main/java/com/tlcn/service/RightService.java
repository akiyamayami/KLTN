package com.tlcn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlcn.dao.CarRepository;
import com.tlcn.dao.RightRepository;
import com.tlcn.model.Right;

@Service
public class RightService {
	@Autowired
	private RightRepository rightRepository;
	
	private Right findOne(int rightID){
		return rightRepository.findOne(rightID);
	}
	
	private List<Right> findAll(){
		List<Right> rights = new ArrayList<>();
		for(Right right : rightRepository.findAll()){
			rights.add(right);
		}
		return rights;
	}
	private void saveRight(Right right){
		rightRepository.save(right);
	}
	
	private void deleteRight(int rightID){
		rightRepository.delete(rightID);
	}
	
	private void updateRight(int rightid,Right right){
		Right rightnow = rightRepository.findOne(rightid);
	}
}
