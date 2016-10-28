package br.com.simplepass.cadevanaluno.dto;

public class OAuthToken {
	String tokenType;
	String value;
	String expiresIn;
	
	public OAuthToken(String tokenType, String value, String expiresIn) {
		super();
		this.tokenType = tokenType;
		this.value = value;
		this.expiresIn = expiresIn;
	}
	
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
}
