package br.com.als.mymoney.api.domain.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Embeddable
@JsonInclude(Include.NON_NULL)
public class Address {

	@Size(min = 5, max = 100)
	private String street;

	@Size(min = 1, max = 15)
	private String num;

	@Size(min = 2, max = 50)
	private String complement;

	@Size(min = 2, max = 50)
	private String district;

	@Size(min = 8, max = 8)
	private String postalCode;

	@Size(min = 2, max = 50)
	private String city;

	@Size(min = 2, max = 50)
	private String state;

	public Address() {
		super();
	}

	public Address(
			String street, 
			String num, 
			String complement, 
			String district, 
			String postalCode, 
			String city,
			String state) {
		super();
		this.street = street;
		this.num = num;
		this.complement = complement;
		this.district = district;
		this.postalCode = postalCode;
		this.city = city;
		this.state = state;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", num=" + num + ", complement=" + complement + ", district=" + district
				+ ", postalCode=" + postalCode + ", city=" + city + ", state=" + state + "]";
	}
}
