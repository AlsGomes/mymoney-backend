package br.com.als.mymoney.api.domain.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.als.mymoney.api.domain.model.dto.PersonDTO;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOInsert;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOUpdate;
import br.com.als.mymoney.api.domain.model.dto.PersonDTOUpdateAddress;
import br.com.als.mymoney.api.domain.services.PersonService;
import br.com.als.mymoney.api.events.ResourceCreatedEvent;

@RestController
@RequestMapping(path = "/persons")
public class PersonController {

	@Autowired
	private PersonService service;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@GetMapping
	public ResponseEntity<List<PersonDTO>> findAll() {
		List<PersonDTO> listDTO = service.findAll();
		return ResponseEntity.ok(listDTO);
	}

	@GetMapping(path = "/{code}")
	public ResponseEntity<PersonDTO> findByCode(@PathVariable String code) {
		PersonDTO objDTO = service.findByCodeOrThrow(code);
		return ResponseEntity.ok(objDTO);
	}

	@PostMapping
	public ResponseEntity<PersonDTO> saveNew(
			@RequestBody @Valid PersonDTOInsert objDTO,
			HttpServletResponse response) {
		
		PersonDTO newObjDTO = service.saveNew(objDTO);
		eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, newObjDTO.getCode()));
		return ResponseEntity.status(HttpStatus.CREATED).body(newObjDTO);
	}
	
	@PutMapping(path = "/{code}")
	public ResponseEntity<PersonDTO> update(
			@PathVariable String code, 
			@RequestBody @Valid PersonDTOUpdate objDTO) {
		
		objDTO.setCode(code);
		PersonDTO objUpdated = service.update(objDTO);
		return ResponseEntity.ok(objUpdated);
	}
	
	@PutMapping(path = "/{code}/address")
	public ResponseEntity<PersonDTO> updateAddress(
			@PathVariable String code, 
			@RequestBody @Valid PersonDTOUpdateAddress objDTO) {
		
		objDTO.setCode(code);
		PersonDTO objUpdated = service.updateAddress(objDTO);
		return ResponseEntity.ok(objUpdated);
	}
	
	@PutMapping(path = "/{code}/active")
	public ResponseEntity<PersonDTO> updateActive(
			@PathVariable String code, 
			@RequestBody Boolean active) {
		
		PersonDTO objUpdated = service.updateActive(code, active);
		return ResponseEntity.ok(objUpdated);
	}
	
	@DeleteMapping(path = "/{code}")
	public ResponseEntity<Void> deleteByCode(@PathVariable String code) {
		service.deleteByCode(code);
		return ResponseEntity.noContent().build();
	}
}
