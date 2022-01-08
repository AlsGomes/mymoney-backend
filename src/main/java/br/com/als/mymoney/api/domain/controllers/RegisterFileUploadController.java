package br.com.als.mymoney.api.domain.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.als.mymoney.api.domain.model.dto.RegisterFileDTO;
import br.com.als.mymoney.api.domain.services.exceptions.DomainException;

@RestController
@RequestMapping(path = "/registers")
public class RegisterFileUploadController {

	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and hasAuthority('SCOPE_write')")
	@PostMapping(path = "/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RegisterFileDTO> uploadTemporaryFile(@Valid RegisterFileDTO file) {
		var fileName = UUID.randomUUID().toString() 
				+ "_"
				+ file.getFile().getOriginalFilename();

		var fullPath = 
				"C:\\Users\\als_0\\Downloads\\docs\\" 
				+ fileName;
		
		var contentType = file.getFile().getContentType();
		var size = file.getFile().getSize();
		
		var obj = RegisterFileDTO.builder()
				.fileName(fileName)
				.contentType(contentType)
				.size(size)
				.build();

		try {
			var outputFile = new File(fullPath);
			if (!outputFile.getParentFile().exists())
				outputFile.getParentFile().mkdirs();

			var output = new FileOutputStream(outputFile);

			output.write(file.getFile().getBytes());
			output.close();
			return ResponseEntity.ok(obj);
		} catch (Exception e) {
			throw new DomainException("Erro ao tentar fazer upload de arquivo", e);
		}
	}
}
