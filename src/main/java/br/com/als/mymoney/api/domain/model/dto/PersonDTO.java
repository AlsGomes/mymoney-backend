package br.com.als.mymoney.api.domain.model.dto;

import br.com.als.mymoney.api.domain.model.Person;

public class PersonDTO {

	private String code;
	private String name;
	private Boolean active;

	public PersonDTO() {
		super();
	}

	public PersonDTO(String code, String name, Boolean active) {
		super();
		this.code = code;
		this.name = name;
		this.active = active;
	}

	public PersonDTO(Person person) {
		super();
		this.code = person.getCode();
		this.name = person.getName();
		this.active = person.getActive();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
