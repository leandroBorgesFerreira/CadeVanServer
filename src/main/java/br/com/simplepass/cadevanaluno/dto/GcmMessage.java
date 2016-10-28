package br.com.simplepass.cadevanaluno.dto;

import java.util.List;

public class GcmMessage {
	private List<String> phoneNumberList;
	private String message;
	private String title;
	private String type;
	
	
	public List<String> getPhoneNumberList() {
		return phoneNumberList;
	}
	public void setPhoneNumberList(List<String> phoneNumberList) {
		this.phoneNumberList = phoneNumberList;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "GcmMessage [phoneNumberList=" + phoneNumberList + ", message=" + message + ", type=" + type + "]";
	}
	
	
	
}
