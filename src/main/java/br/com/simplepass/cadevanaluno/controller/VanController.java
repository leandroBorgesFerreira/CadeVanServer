package br.com.simplepass.cadevanaluno.controller;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.simplepass.cadevanaluno.domain.Van;
import br.com.simplepass.cadevanaluno.dto.VansManager;
import br.com.simplepass.cadevanaluno.exception.ResourceNotFoundException;
import io.swagger.annotations.ApiOperation;


@RestController("vanController")
public class VanController {
	@Inject
	private VansManager vansManager;

	@RequestMapping(value={"/vans"}, method=RequestMethod.GET)
	@ApiOperation(value="Retrives all vans in a Map", response=Van.class, responseContainer="Map")
	public ResponseEntity<Map<Integer, Van>> getAllVans(){
		return new ResponseEntity<>(vansManager.getVansMap(), HttpStatus.OK);
	}
	
	@RequestMapping(value={"/vans/{vanId}"}, method=RequestMethod.GET)
	public ResponseEntity<Van> getVanById(@PathVariable Integer vanId){
		Van van = vansManager.getVanById(vanId);
		
		if(van == null){
			throw new ResourceNotFoundException("Van with id: " + vanId +
					" not found.");
		} else{
			return new ResponseEntity<>(van, HttpStatus.OK);
		}
		
	}
	
	@RequestMapping(value={"/vans"}, method=RequestMethod.POST)
	public ResponseEntity<Void> putVan(@RequestBody Van van){
		vansManager.updateVan(van.getVanId(), van);
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
