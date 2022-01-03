package br.com.als.mymoney.api.domain.model.dto.statistics;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import br.com.als.mymoney.api.domain.model.Person;
import br.com.als.mymoney.api.domain.model.RegisterType;
import br.com.als.mymoney.api.domain.model.dto.PersonDTO;

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

	public RegisterStatisticsByPerson(PersonDTO person, RegisterType type, BigDecimal total) {
		this.person = person;
		this.total = total;
		this.type = type;
	}

	public PersonDTO getPerson() {
		return person;
	}

	public void setPerson(PersonDTO person) {
		this.person = person;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public RegisterType getType() {
		return type;
	}

	public void setType(RegisterType type) {
		this.type = type;
	}
}