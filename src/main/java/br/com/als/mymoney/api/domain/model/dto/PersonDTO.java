package br.com.als.mymoney.api.domain.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.als.mymoney.api.domain.model.Address;
import br.com.als.mymoney.api.domain.model.Person;
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
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@JsonInclude(Include.NON_NULL)
public class PersonDTO {

	@Getter
	@EqualsAndHashCode.Include
	private String code;

	@Getter
	private String name;

	private Boolean active;

	@Getter
	private Address address;

	@Getter
	private final List<ContactDTO> contacts = new ArrayList<>();

	public PersonDTO(Person person) {
		this.code = person.getCode();
		this.name = person.getName();
		this.active = person.isActive();
		this.address = person.getAddress();
		this.contacts.addAll(person.getContacts().stream().map(ContactDTO::new).collect(Collectors.toList()));
	}

	public Boolean isActive() {
		return active;
	}
}
