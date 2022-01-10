package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.als.mymoney.api.domain.model.RegisterFile;
import br.com.als.mymoney.api.domain.model.dto.validation.FileContentType;
import br.com.als.mymoney.api.domain.model.dto.validation.FileSize;
import br.com.als.mymoney.api.domain.services.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@ToString
public class RegisterFileDTO {

	@NotNull
	@FileContentType(allowed = { 
			MediaType.APPLICATION_PDF_VALUE, 
			MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	@FileSize(max = "3MB")
	private MultipartFile file;

	private String code;
	private String fileName;
	private long size;
	private String contentType;

	public RegisterFileDTO(RegisterFile registerFile) {
		this.fileName = registerFile.getFileName();
		this.size = registerFile.getSize();
		this.contentType = registerFile.getContentType();
		this.code = registerFile.getCode();
	}

	public RegisterFileDTO(FileStorageService.File file) {
		this.fileName = file.getFileName();
		this.size = file.getSize();
		this.contentType = file.getContentType();
	}
}
