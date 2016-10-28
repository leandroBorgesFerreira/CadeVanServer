package br.com.simplepass.cadevanaluno;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationManager extends AuthorizationServerConfigurerAdapter {
	@Inject
	private AuthenticationManager authenticationManager;
	
	@Inject
	private DataSource dataSource; 
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception{
		endpoints
			.tokenStore(tokenStore())
			.authenticationManager(this.authenticationManager);		
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception{
		clients.inMemory()
			.withClient("app")
			.secret("top_secret")
			.authorizedGrantTypes("authorization_code", "implicit", "password") 
			.authorities("ROLE_CLIENT") 
			.scopes("read", "write")
			.resourceIds("CadeVanAluno_Resources")
			.accessTokenValiditySeconds(0);	
	}
	
	@Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
	
}
