package br.com.als.mymoney.api.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.als.mymoney.api.domain.model.dto.RegisterFileDTO;
import br.com.als.mymoney.api.domain.services.FileStorageService;
import br.com.als.mymoney.api.domain.services.RegisterService;

@RestController
@RequestMapping(path = "/registers")
public class RegisterFileUploadController {

	@Autowired
	private FileStorageService service;
	
	@Autowired
	private RegisterService registerService;

	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and hasAuthority('SCOPE_write')")
	@PostMapping(path = "/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RegisterFileDTO> uploadTemporaryFile(@Valid RegisterFileDTO file) throws IOException {

		var fileName = service.generateName(file.getFile().getOriginalFilename());
		var contentType = file.getFile().getContentType();
		var size = file.getFile().getSize();
		var inputStream = file.getFile().getInputStream();
		
		var savedFile = service.save(FileStorageService.File.builder()
				.inputStream(inputStream)
				.contentType(contentType)
				.fileName(fileName)
				.size(size)
				.build());

		var obj = new RegisterFileDTO(savedFile);
		return ResponseEntity.ok(obj);
	}
	
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	@GetMapping(
			path = "/{registerCode}/attachments/{fileName}", 
			produces = { 
					MediaType.IMAGE_JPEG_VALUE,
					MediaType.IMAGE_PNG_VALUE, 
					MediaType.APPLICATION_PDF_VALUE })
	public ResponseEntity<?> getFile(
			@PathVariable String registerCode, 
			@PathVariable String fileName)
			throws IOException {
		
		registerService.findRegisterFileByFileName(registerCode, fileName);		
		
		var file = service.get(registerCode, fileName);
		
		if (file.hasBytes()) {
			return ResponseEntity
					.ok()
					.header(HttpHeaders.CONTENT_TYPE, file.getContentType())
					.body(file.getInputStream().readAllBytes());
		} else {
			return ResponseEntity
					.status(HttpStatus.FOUND)
					.header(HttpHeaders.LOCATION, file.getUrl())
					.build();
		}
	}
	
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	@GetMapping(path = "/{registerCode}/attachments/{fileName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisterFileDTO> getFileJson(
			@PathVariable String registerCode, 
			@PathVariable String fileName)
			throws IOException {
		
		var obj = registerService.findRegisterFileByFileName(registerCode, fileName);		
		
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(obj);
	}
}
