package com.tlcn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tlcn.dao.SttCarRepository;
import com.tlcn.model.SttCar;

@Service
public class SttCarService {
	@Autowired
	private SttCarRepository sttCarRepository;

	public SttCarService() {
		super();
	}
	
	public List<SttCar> findAll(){
		List<SttCar> stts = new ArrayList<>();
		for(SttCar s : sttCarRepository.findAll()){
			stts.add(s);
		}
		return stts;
	}
	public void save(SttCar stt){
		sttCarRepository.save(stt);
	}
	
	public SttCar findOne(int sttcarID){
		return sttCarRepository.findOne(sttcarID);
	}
}
