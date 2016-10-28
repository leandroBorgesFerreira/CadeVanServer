package br.com.simplepass.cadevanaluno.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.simplepass.cadevanaluno.domain.User;


public interface UsersRepository extends CrudRepository<User, Long>{
	public User findByPhoneNumber(String phone);
	public User findById(Long id);
	
}
