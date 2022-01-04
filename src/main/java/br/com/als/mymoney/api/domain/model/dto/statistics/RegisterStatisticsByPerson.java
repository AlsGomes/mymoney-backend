package br.com.als.mymoney.api.domain.model.dto.statistics;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.RegisterType;
import br.com.als.mymoney.api.domain.model.dto.PersonDTO;
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
public class RegisterStatisticsByPerson {
	private PersonDTO person;
	private RegisterType type;
	private BigDecimal total;

	public RegisterStatisticsByPerson(Person person, RegisterType type, BigDecimal total) {
		this.person = new PersonDTO();
		BeanUtils.copyProperties(person, this.person);

		this.total = total;
		this.type = type;
	}
}