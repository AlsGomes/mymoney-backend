package br.com.als.mymoney.api.domain.model.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.RegisterType;
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
public class RegisterDTO {

	@EqualsAndHashCode.Include
	private String code;
	private String description;
	private String dueDate;
	private String paymentDate;
	private BigDecimal value;
	private String obs;
	private RegisterType type;
	private CategoryDTO category;
	private PersonDTO person;

	public RegisterDTO(Register register) {
		this.code = register.getCode();
		this.description = register.getDescription();
		this.dueDate = register.getDueDate().toString();
		this.paymentDate = register.getPaymentDate() == null ? null : register.getPaymentDate().toString();
		this.value = register.getValue();
		this.obs = register.getObs();
		this.type = register.getType();
		this.category = new CategoryDTO(register.getCategory());
		this.person = new PersonDTO(register.getPerson());
	}
}
