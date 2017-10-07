package com.tlcn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlcn.dao.SttDriverRepository;
import com.tlcn.model.SttDriver;

@Service
public class SttDriverService {
	@Autowired
	private SttDriverRepository sttDriverRepository;

	public SttDriverService() {
		super();
	}
	
	public SttDriver findOne(int sttdriverID){
		return sttDriverRepository.findOne(sttdriverID);
	}
	
	public List<SttDriver> findAll(){
		List<SttDriver> stts = new ArrayList<>();
		for(SttDriver s : sttDriverRepository.findAll()){
			stts.add(s);
		}
		return stts;
	}
	public void save(SttDriver stt){
		sttDriverRepository.save(stt);
	}
}
