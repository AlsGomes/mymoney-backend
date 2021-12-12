package br.com.als.mymoney.api.domain.model.dto;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.als.mymoney.api.domain.model.RegisterType;
import br.com.als.mymoney.api.domain.model.dto.validation.AnyValueOfEnum;
import br.com.als.mymoney.api.domain.model.dto.validation.DateTimeFormatWithPattern;

@JsonInclude(Include.NON_NULL)
public class RegisterDTOUpdate {

	@NotBlank
	@Size(min = 1, max = 50)
	private String description;

	@DateTimeFormatWithPattern(pattern = "yyyy-MM-dd")
	@NotBlank
	private String dueDate;

	@DateTimeFormatWithPattern(pattern = "yyyy-MM-dd")	
	private String paymentDate;

	@NotNull
	@DecimalMin(value = "0.00")
	private BigDecimal value;
	
	@Size(min = 1, max = 100)
	private String obs;

	@NotBlank
	@AnyValueOfEnum(enumClass = RegisterType.class)
	private String type;

	@NotNull
	@Valid
	private CategoryDTOCodeNonNull category;

	@NotNull
	@Valid
	private PersonDTOCodeNonNull person;

	public RegisterDTOUpdate() {}

	public RegisterDTOUpdate(
			String description,
			String dueDate,
			String paymentDate,
			BigDecimal value,
			String obs,
			String type,
			CategoryDTOCodeNonNull category,
			PersonDTOCodeNonNull person) {		
		this.description = description;
		this.dueDate = dueDate;
		this.paymentDate = paymentDate;
		this.value = value;
		this.obs = obs;
		this.type = type;
		this.category = category;
		this.person = person;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public CategoryDTOCodeNonNull getCategory() {
		return category;
	}

	public void setCategory(CategoryDTOCodeNonNull category) {
		this.category = category;
	}

	public PersonDTOCodeNonNull getPerson() {
		return person;
	}

	public void setPerson(PersonDTOCodeNonNull person) {
		this.person = person;
	}
}
