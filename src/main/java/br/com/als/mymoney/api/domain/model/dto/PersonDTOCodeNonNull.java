package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonInclude(Include.NON_NULL)
public class PersonDTOCodeNonNull {

	@EqualsAndHashCode.Include
	@NotBlank
	private String code;

	public PersonDTOCodeNonNull(Person person) {
		this.code = person.getCode();
	}
}
