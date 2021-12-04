package br.com.als.mymoney.api.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.als.mymoney.api.domain.model.Address;
import br.com.als.mymoney.api.domain.model.Person;

@JsonInclude(Include.NON_NULL)
public class PersonDTO {

	private String code;
	private String name;
	private Boolean active;
	private Address address;

	public PersonDTO() {
		super();
	}

	public PersonDTO(String code, String name, Boolean active, Address address) {
		super();
		this.code = code;
		this.name = name;
		this.active = active;
		this.address = address;
	}

	public PersonDTO(Person person) {
		super();
		this.code = person.getCode();
		this.name = person.getName();
		this.active = person.isActive();
		this.address = person.getAddress();
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

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
