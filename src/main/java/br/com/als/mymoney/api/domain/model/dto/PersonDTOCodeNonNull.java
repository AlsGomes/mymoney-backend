package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.als.mymoney.api.domain.model.Person;

@JsonInclude(Include.NON_NULL)
public class PersonDTOCodeNonNull {

	@NotBlank
	private String code;

	public PersonDTOCodeNonNull() {
		super();
	}

	public PersonDTOCodeNonNull(String code) {
		super();
		this.code = code;
	}

	public PersonDTOCodeNonNull(Person person) {
		super();
		this.code = person.getCode();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
