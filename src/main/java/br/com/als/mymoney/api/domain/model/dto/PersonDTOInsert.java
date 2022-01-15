package br.com.als.mymoney.api.domain.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.als.mymoney.api.domain.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonDTOInsert {

	@NotBlank
	@Size(min = 1, max = 50)
	private String name;

	private Boolean active;

	@Valid
	private Address address;

	@Valid
	private final List<ContactDTOInsert> contacts = new ArrayList<>();
}
