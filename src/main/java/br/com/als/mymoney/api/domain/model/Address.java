package br.com.als.mymoney.api.domain.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@ToString
@JsonInclude(Include.NON_NULL)
@Embeddable
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
}
