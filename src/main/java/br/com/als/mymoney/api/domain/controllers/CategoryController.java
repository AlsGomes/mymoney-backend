package br.com.als.mymoney.api.domain.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.als.mymoney.api.domain.model.dto.CategoryDTO;
import br.com.als.mymoney.api.domain.model.dto.CategoryDTOInsert;
import br.com.als.mymoney.api.domain.services.CategoryService;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> listDTO = service.findAll();
		return ResponseEntity.ok(listDTO);
	}
	
	@GetMapping(path = "/{code}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable String code) {
		CategoryDTO objDTO = service.findByIdOrThrow(code);		
		return ResponseEntity.ok(objDTO);
	}

	@PostMapping
	public ResponseEntity<CategoryDTO> saveNew(@RequestBody @Valid CategoryDTOInsert objDTO) {		
		CategoryDTO newObjDTO = service.saveNew(objDTO);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{code}")
				.buildAndExpand(newObjDTO.getCode())
				.toUri();
		return ResponseEntity.created(uri).body(newObjDTO);
	}

}
