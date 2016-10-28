package br.com.simplepass.cadevanaluno.dto;

import java.util.List;
import java.util.Map;

public class FirebasePushRequest {
	List<String> registration_ids;
	String to;
	boolean content_available;
	Map<String, String> notification;
	Map<String, String> data;
	public FirebasePushRequest(List<String> registration_ids, boolean content_available,
			Map<String, String> notification, Map<String, String> data) {
		
		if(registration_ids.size() == 1){
			this.to = registration_ids.get(0);
		} else{
			this.registration_ids = registration_ids;
		}
		
		this.content_available = content_available;
		this.notification = notification;
		this.data = data;
	}
	
	
}
