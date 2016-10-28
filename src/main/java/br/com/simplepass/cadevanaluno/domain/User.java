package br.com.simplepass.cadevanaluno.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue
	@Column(name="USER_ID")
	private Long id;
	
	@Column(name="EMAIL")
	@NotEmpty
	private String email;
	
	@Column(name="PASSWORD")
	@NotEmpty
	@JsonProperty(access = Access.WRITE_ONLY)
    private String password;
	
	@Column(name="NAME")
	@NotEmpty
    private String name;
	
	@Column(name="PHONE_NUMBER")
	@NotEmpty
    private String phoneNumber;
	
	@Column(name="GCM_TOKEN")
	private String gcmToken;
	
	@Column(name="OS")
	private String os;
	
	@Transient
	private String accessToken;
		  
	public User() {
	}
	
	public User(String email, String password, String name, String phoneNumber) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	public Long getId() {
		return id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getGcmToken() {
		return gcmToken;
	}
	public void setGcmToken(String gcmToken) {
		this.gcmToken = gcmToken;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}    
}
