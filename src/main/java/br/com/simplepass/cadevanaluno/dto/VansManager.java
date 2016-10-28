package br.com.simplepass.cadevanaluno.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.simplepass.cadevanaluno.domain.Van;
import br.com.simplepass.cadevanaluno.utils.Constants;

@Component
public class VansManager {
	private Map<Integer, Van> mVansMap;

	public VansManager() {
		super();
		this.mVansMap = new HashMap<>();
	}
	
	public void updateVan(Integer vanId, Van van){
		van.setLastUpdate(System.currentTimeMillis());
		mVansMap.put(vanId, van);
	}
	
	public Van getVanById(Integer id){
		Van van = mVansMap.get(id);
		
		if(van == null){
			return null;
		}
		
		if((System.currentTimeMillis() - van.getLastUpdate()) < Constants.TIME.FIVE_MINUTES){
			return van;
		} else{
			return null;
		}
	}
	
	public List<Van> getVansAsList(){		
		ArrayList<Van> answerList = new ArrayList<>();
		
		for(Van van : mVansMap.values()){
			if((System.currentTimeMillis() - van.getLastUpdate()) < Constants.TIME.FIVE_MINUTES){
				answerList.add(van);
			}
		}
		
		return answerList;
	}
	public Map<Integer, Van> getVansMap(){
		return mVansMap;
	}
}
