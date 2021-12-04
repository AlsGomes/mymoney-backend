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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.als.mymoney.api.domain.model.dto.RegisterDTO;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOInsert;
import br.com.als.mymoney.api.domain.repositories.filters.RegisterFilter;
import br.com.als.mymoney.api.domain.services.RegisterService;
import br.com.als.mymoney.api.events.ResourceCreatedEvent;

@RestController
@RequestMapping(path = "/registers/{personCode}")
public class RegisterController {

	@Autowired
	private RegisterService service;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@GetMapping
	public ResponseEntity<List<RegisterDTO>> findAll(@PathVariable String personCode) {
		List<RegisterDTO> listDTO = service.findAll(personCode);
		return ResponseEntity.ok(listDTO);
	}

	@GetMapping(path = "/{code}")
	public ResponseEntity<RegisterDTO> findByCode(
			@PathVariable String personCode, 
			@PathVariable String code) {

		RegisterDTO objDTO = service.findByCodeOrThrow(personCode, code);
		return ResponseEntity.ok(objDTO);
	}

	@GetMapping(path = "/filter")
	public ResponseEntity<List<RegisterDTO>> search(
			@PathVariable String personCode, 
			RegisterFilter filter) {

		List<RegisterDTO> listDTO = service.search(personCode, filter);
		return ResponseEntity.ok(listDTO);
	}

	@PostMapping
	public ResponseEntity<RegisterDTO> saveNew(
			@PathVariable String personCode,
			@RequestBody @Valid RegisterDTOInsert objDTO, 
			HttpServletResponse response) {

		RegisterDTO objNew = service.saveNew(personCode, objDTO);
		eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, objNew.getCode()));
		return ResponseEntity.status(HttpStatus.CREATED).body(objNew);
	}

	@DeleteMapping(path = "/{code}")
	public ResponseEntity<Void> delete(
			@PathVariable String personCode, 
			@PathVariable String code) {
		
		service.delete(personCode, code);
		return ResponseEntity.noContent().build();
	}
}
