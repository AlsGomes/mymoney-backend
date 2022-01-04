package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.als.mymoney.api.domain.model.Address;
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
public class PersonDTOInsert {

	@NotBlank
	@Size(min = 1, max = 50)
	private String name;

	private Boolean active;

	@Valid
	private Address address;
}
