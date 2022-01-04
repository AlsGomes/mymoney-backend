package br.com.als.mymoney.api.domain.model.dto;

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
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class PersonDTOSummary {

	@EqualsAndHashCode.Include
	private String code;
	private String name;

	public PersonDTOSummary(Person person) {
		this.name = person.getName();
		this.code = person.getCode();
	}
}
