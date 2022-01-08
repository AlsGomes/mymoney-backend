package br.com.als.mymoney.api.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.als.mymoney.api.domain.model.RegisterFile;
import br.com.als.mymoney.api.domain.model.dto.validation.NullButNotEmpty;
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
public class RegisterFileDTOUpdate {
	@NullButNotEmpty
	private String code;
	
	private String fileName;
	private long size;
	private String contentType;

	public RegisterFileDTOUpdate(RegisterFile registerFile) {
		this.code = registerFile.getCode();
		this.fileName = registerFile.getFileName();
		this.size = registerFile.getSize();
		this.contentType = registerFile.getContentType();
	}
}
