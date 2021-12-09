package br.com.als.mymoney.api.domain.model.dto;

import br.com.als.mymoney.api.domain.model.Person;

public class PersonDTOSummary {

	private String code;
	private String name;

	public PersonDTOSummary() {
	}

	public PersonDTOSummary(String name, String code) {		
		this.name = name;
		this.code = code;
	}
	
	public PersonDTOSummary(Person person) {		
		this.name = person.getName();
		this.code = person.getCode();
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
}
