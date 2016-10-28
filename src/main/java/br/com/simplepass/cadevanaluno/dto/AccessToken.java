package br.com.simplepass.cadevanaluno.dto;

public class AccessToken {
	String access_token;
	String token_type;
	String scope;
	public AccessToken(String access_token, String token_type, String scope) {
		super();
		this.access_token = access_token;
		this.token_type = token_type;
		this.scope = scope;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
}
