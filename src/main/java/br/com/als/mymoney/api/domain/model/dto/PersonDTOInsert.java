package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.als.mymoney.api.domain.model.Address;

public class PersonDTOInsert {

	@NotBlank
	@Size(min = 1, max = 50)
	private String name;

	private Boolean active;

	@Valid
	private Address address;

	public PersonDTOInsert() {
		super();
	}

	public PersonDTOInsert(String name, Boolean active, Address address) {
		super();
		this.name = name;
		this.active = active;
		this.address = address;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
