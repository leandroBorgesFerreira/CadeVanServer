package br.com.simplepass.cadevanaluno.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.simplepass.cadevanaluno.domain.Driver;
import br.com.simplepass.cadevanaluno.dto.RecoverPasswordBean;
import br.com.simplepass.cadevanaluno.exception.BadRequestException;
import br.com.simplepass.cadevanaluno.exception.ResourceNotFoundException;
import br.com.simplepass.cadevanaluno.repository.DriversRepository;
import br.com.simplepass.cadevanaluno.utils.Utils;
import io.swagger.annotations.ApiOperation;

@RestController("driverController")
public class DriverController {
	@Inject
	private DriversRepository driversRepository;
	
	@Inject
	private JavaMailSender javaMailSender;
	
	@RequestMapping(value="/drivers/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Driver> updateUser(@PathVariable Long id, @RequestBody Driver driverUpdated){
		if(id != driverUpdated.getId()){
			throw new BadRequestException("The ID in the URL and in the User object are not the same.");
		}
		
		Driver driver = driversRepository.findById(id);
		
		if(driver == null){
			throw new ResourceNotFoundException("Driver with ID: " + driverUpdated.getId() +
					" not found.");
		} else{				
			driverUpdated.setPassword(driver.getPassword());
			Driver driverReturn = driversRepository.save(driverUpdated);
			
			return new ResponseEntity<>(driverReturn, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/drivers", method=RequestMethod.GET)
	@ApiOperation(value="Retrives all drivers", response=Driver.class, responseContainer="List")
	public ResponseEntity<Iterable<Driver>> getAllDrivers(
			@RequestParam(value="phoneNumber", required=false) String phoneNumber, 
			@RequestParam(value="trackingCode", required=false) Long trackingCode){
		if(phoneNumber != null){
			List<Driver> driverList = new ArrayList<>();
			
			Driver driver = driversRepository.findByPhoneNumber(phoneNumber);
			if(driver != null){
				driverList.add(driver);
				return new ResponseEntity<>(driverList, HttpStatus.OK);
			} else{
				throw new ResourceNotFoundException("Driver with phone: " + phoneNumber +
						" not found.");
			}
		} else if(trackingCode != null){
			List<Driver> driverList = new ArrayList<>();
			
			Driver driver = driversRepository.findByTrackingCode(trackingCode);
			if(driver != null){
				driverList.add(driver);
				return new ResponseEntity<>(driverList, HttpStatus.OK);
			} else{
				throw new ResourceNotFoundException("Driver with trackingCode: " + trackingCode +
						" not found.");
			}
		} else{	
			return new ResponseEntity<>(driversRepository.findAll(), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/drivers", method=RequestMethod.POST)
	public ResponseEntity<Driver> createDriver(@Valid @RequestBody Driver driver){
		driver = driversRepository.save(driver);
		
		driver.setTrackingCode(driver.getId() + Driver.TRACKING_CODE_BASE);
		Driver driverWithTrackingCode = driversRepository.save(driver);
		
		return new ResponseEntity<>(driverWithTrackingCode, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/drivers/recoverPassword", method=RequestMethod.POST)
	public ResponseEntity<Driver> recoverPassword(@RequestBody RecoverPasswordBean passwordBean){
		Driver driver = driversRepository.findByPhoneNumber(passwordBean.getPhoneNumber());
		
		if(driver == null){
			throw new ResourceNotFoundException("Driver with phone: " + passwordBean.getPhoneNumber() +
					" not found.");
		}
		
		String email = driver.getEmail();
		
		try{
			String encrypt = Utils.md5(String.valueOf(driver.getId()));
			
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(email);
			mailMessage.setSubject("Recuperar senha");
			mailMessage.setText("Olá, " + driver.getName() + ".\n\n "+ 
					"Você solicitou a recuperação de senha do CadeVan. Acesse o link : \n\n" + 
					"http://simplepass.teramundi.com/cadevanmotorista/changePassword/changePassword.php?encrypt="+encrypt+"&action=reset' \n\n" +
					"Para redefinir a sua senha. \n\n" +
					"Atenciosamente, \n" +
					"Equipe CadeVan/Simple Pass");
			mailMessage.setFrom("contato@simplepass.com.br");
			
			System.out.println(encrypt);
			
			javaMailSender.send(mailMessage);
			
			return new ResponseEntity<>(driver, HttpStatus.OK);
		} catch(NoSuchAlgorithmException e){			
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
