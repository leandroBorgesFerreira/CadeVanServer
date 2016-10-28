package br.com.simplepass.cadevanaluno.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="drivers")
public class Driver {
	@Id
	@GeneratedValue
	@Column(name="DRIVER_ID")
	private Long id;
	
	@Column(name="PHONE_NUMBER")
	@NotEmpty
    private String phoneNumber;
	
	@Column(name="PASSWORD")
	@NotEmpty
	@JsonProperty(access = Access.WRITE_ONLY)
    private String password;
	
	@Column(name="NAME")
	@NotEmpty
    private String name;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="GCM_TOKEN")
	private String gcmToken;
	
	@Column(name="OS")
	private String os;
	
	@Column(name="TRACKING_CODE")
	private Long trackingCode;
	
	@Column(name="COMPANY")
	private String company;
	
	public static final Long TRACKING_CODE_BASE = 1135L;

	public Driver() {
	}

	public Driver(Long id, String phoneNumber, String password, String name, String email, String company) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.name = name;
		this.email = email;
		this.company = company;
		
		if(id != null){
			this.trackingCode = TRACKING_CODE_BASE + id;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGcmToken() {
		return gcmToken;
	}

	public void setGcmToken(String gcmToken) {
		this.gcmToken = gcmToken;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public Long getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(Long trackingCode) {
		this.trackingCode = trackingCode;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
}
