package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.Valid;

import br.com.als.mymoney.api.domain.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class PersonDTOUpdateAddress {

	@EqualsAndHashCode.Include
	private String code;

	@Valid
	private Address address;
}
