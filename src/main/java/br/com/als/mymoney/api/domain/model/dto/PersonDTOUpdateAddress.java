package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.Valid;

import br.com.als.mymoney.api.domain.model.Address;

public class PersonDTOUpdateAddress {

	private String code;

	@Valid
	private Address address;

	public PersonDTOUpdateAddress() {
		super();
	}

	public PersonDTOUpdateAddress(String name, Boolean active, String code, Address address) {
		super();
		this.code = code;
		this.address = address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
