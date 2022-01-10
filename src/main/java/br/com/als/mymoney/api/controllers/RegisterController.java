package br.com.als.mymoney.api.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.als.mymoney.api.controllers.utils.SimplePage;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTO;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOInsert;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOSummary;
import br.com.als.mymoney.api.domain.model.dto.RegisterDTOUpdate;
import br.com.als.mymoney.api.domain.repositories.filters.RegisterFilter;
import br.com.als.mymoney.api.domain.services.RegisterService;
import br.com.als.mymoney.api.events.ResourceCreatedEvent;

@RestController
@RequestMapping(path = "/registers")
public class RegisterController {

	@Autowired
	private RegisterService service;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	@GetMapping
	public ResponseEntity<List<RegisterDTO>> findAll() {
		List<RegisterDTO> listDTO = service.findAll();
		return ResponseEntity.ok(listDTO);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	@GetMapping(path = "/{code}")
	public ResponseEntity<RegisterDTO> findByCode(@PathVariable String code) {
		RegisterDTO objDTO = service.findByCodeOrThrow(code);
		return ResponseEntity.ok(objDTO);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	@GetMapping(path = "/filter")
	public ResponseEntity<SimplePage<RegisterDTO>> search(			
			RegisterFilter filter,
			Pageable pageable) {

		SimplePage<RegisterDTO> listDTO = service.search(filter, pageable);
		return ResponseEntity.ok(listDTO);
	}
	
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	@GetMapping(path = "/filter", params = "summary")
	public ResponseEntity<SimplePage<RegisterDTOSummary>> searchSummary(			
			RegisterFilter filter,
			Pageable pageable) {

		SimplePage<RegisterDTOSummary> listDTO = service.searchSummary(filter, pageable);
		return ResponseEntity.ok(listDTO);
	}	

	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and hasAuthority('SCOPE_write')")
	@PostMapping
	public ResponseEntity<RegisterDTO> saveNew(			
			@RequestBody @Valid RegisterDTOInsert objDTO, 
			HttpServletResponse response) {

		RegisterDTO objNew = service.saveNew(objDTO);
		eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, objNew.getCode()));
		return ResponseEntity.status(HttpStatus.CREATED).body(objNew);
	}
	
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and hasAuthority('SCOPE_write')")
	@PutMapping("/{code}")
	public ResponseEntity<RegisterDTO> update(			
			@PathVariable String code,
			@RequestBody @Valid RegisterDTOUpdate objDTO, 
			HttpServletResponse response) {

		RegisterDTO objUpdate = service.update(code, objDTO);		
		return ResponseEntity.ok().body(objUpdate);
	}

	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and hasAuthority('SCOPE_write')")
	@DeleteMapping(path = "/{code}")
	public ResponseEntity<Void> delete(@PathVariable String code) {
		service.delete(code);
		return ResponseEntity.noContent().build();
	}
}
