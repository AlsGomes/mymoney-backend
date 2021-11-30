package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PersonDTOInsert {

	@NotBlank
	@Size(min = 1, max = 50)
	private String name;

	private Boolean active;

	public PersonDTOInsert() {
		super();
	}

	public PersonDTOInsert(String name, Boolean active) {
		super();
		this.name = name;
		this.active = active;
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
}
