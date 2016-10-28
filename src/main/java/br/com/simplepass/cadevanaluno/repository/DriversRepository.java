package br.com.simplepass.cadevanaluno.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.simplepass.cadevanaluno.domain.Driver;

public interface DriversRepository extends CrudRepository<Driver, Long>{
	public Driver findByPhoneNumber(String phone);
	public Driver findByTrackingCode(Long trackingCode);
	public Driver findById(Long id);
}
