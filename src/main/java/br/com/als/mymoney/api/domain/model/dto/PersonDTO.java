package br.com.als.mymoney.api.domain.model.dto;

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

	public PersonDTO(Person person) {		
		this.code = person.getCode();
		this.name = person.getName();
		this.active = person.isActive();
		this.address = person.getAddress();
	}

	public Boolean isActive() {
		return active;
	}
}
