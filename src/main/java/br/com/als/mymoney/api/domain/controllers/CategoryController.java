package br.com.als.mymoney.api.domain.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.als.mymoney.api.domain.model.dto.CategoryDTO;
import br.com.als.mymoney.api.domain.model.dto.CategoryDTOInsert;
import br.com.als.mymoney.api.domain.services.CategoryService;
import br.com.als.mymoney.api.events.ResourceCreatedEvent;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and hasAuthority('SCOPE_read')")
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> listDTO = service.findAll();
		return ResponseEntity.ok(listDTO);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and hasAuthority('SCOPE_read')")
	@GetMapping(path = "/{code}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable String code) {
		CategoryDTO objDTO = service.findByCodeOrThrow(code);
		return ResponseEntity.ok(objDTO);
	}

	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and hasAuthority('SCOPE_write')")
	@PostMapping
	public ResponseEntity<CategoryDTO> saveNew(
			@RequestBody @Valid CategoryDTOInsert objDTO,
			HttpServletResponse response) {
		
		CategoryDTO newObjDTO = service.saveNew(objDTO);
		eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, newObjDTO.getCode()));
		return ResponseEntity.status(HttpStatus.CREATED).body(newObjDTO);
	}

}
