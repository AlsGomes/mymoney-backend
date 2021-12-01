package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PersonDTOUpdate {

	private String code;

	@NotBlank
	@Size(min = 1, max = 50)
	private String name;

	private Boolean active;

	public PersonDTOUpdate() {
		super();
	}

	public PersonDTOUpdate(String name, Boolean active, String code) {
		super();
		this.name = name;
		this.active = active;
		this.code = code;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
}
