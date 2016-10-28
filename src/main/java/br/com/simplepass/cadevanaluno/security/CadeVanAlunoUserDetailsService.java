package br.com.simplepass.cadevanaluno.security;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.simplepass.cadevanaluno.domain.Driver;
import br.com.simplepass.cadevanaluno.domain.User;
import br.com.simplepass.cadevanaluno.repository.DriversRepository;
import br.com.simplepass.cadevanaluno.repository.UsersRepository;

@Component
public class CadeVanAlunoUserDetailsService implements UserDetailsService{
	@Inject
	private UsersRepository usersRepository;
	@Inject
	private DriversRepository driversRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = usersRepository.findByPhoneNumber(username);
		Driver driver = driversRepository.findByPhoneNumber(username);
		if(user == null &&  driver == null){
			throw new UsernameNotFoundException(String.format("Usuário: %s não existe",
					username));
		}
		
		List<GrantedAuthority> authorithies = new ArrayList<>();
		UserDetails userDetails;
		
		if(user != null){
			authorithies = AuthorityUtils.createAuthorityList("ROLE_CLIENT");
			userDetails = new org.springframework.security.core.userdetails.User(
				user.getPhoneNumber(), user.getPassword(), authorithies);
		} else{
			authorithies = AuthorityUtils.createAuthorityList("ROLE_CLIENT");
			userDetails = new org.springframework.security.core.userdetails.User(
					driver.getPhoneNumber(), driver.getPassword(), authorithies);
		}
		
		return userDetails;
	}
	
}
