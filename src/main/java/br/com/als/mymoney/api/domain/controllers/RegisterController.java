package br.com.als.mymoney.api.domain.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.als.mymoney.api.domain.model.dto.RegisterDTO;
import br.com.als.mymoney.api.domain.services.RegisterService;

@RestController
@RequestMapping(path = "/registers")
public class RegisterController {

	@Autowired
	private RegisterService service;

	@GetMapping
	public ResponseEntity<List<RegisterDTO>> findAll() {
		List<RegisterDTO> listDTO = service.findAll();
		return ResponseEntity.ok(listDTO);
	}

	@GetMapping(path = "/{code}")
	public ResponseEntity<RegisterDTO> findByCode(@PathVariable String code) {
		RegisterDTO objDTO = service.findByCodeOrThrow(code);
		return ResponseEntity.ok(objDTO);
	}

	//TODO Trocar de controlador para usar no PersonService
	@GetMapping(path = "/person/{code}")
	public ResponseEntity<List<RegisterDTO>> findAllByPerson(@PathVariable String code) {
		List<RegisterDTO> listDTO = service.findByPersonOrThrow(code);
		return ResponseEntity.ok(listDTO);
	}
}
