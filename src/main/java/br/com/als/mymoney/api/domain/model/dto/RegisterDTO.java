package br.com.als.mymoney.api.domain.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.RegisterType;

@JsonInclude(Include.NON_NULL)
public class RegisterDTO {

	private String code;
	private String description;
	private LocalDate dueDate;
	private LocalDate paymentDate;
	private BigDecimal value;
	private String obs;
	private RegisterType type;
	private CategoryDTO category;
	private PersonDTO person;

	public RegisterDTO() {
		super();
	}

	public RegisterDTO(String code, String description, LocalDate dueDate, LocalDate paymentDate, BigDecimal value,
			String obs, RegisterType type, CategoryDTO category, PersonDTO person) {
		super();
		this.code = code;
		this.description = description;
		this.dueDate = dueDate;
		this.paymentDate = paymentDate;
		this.value = value;
		this.obs = obs;
		this.type = type;
		this.category = category;
		this.person = person;
	}

	public RegisterDTO(Register register) {
		super();
		this.code = register.getCode();
		this.description = register.getDescription();
		this.dueDate = register.getDueDate();
		this.paymentDate = register.getPaymentDate();
		this.value = register.getValue();
		this.obs = register.getObs();
		this.type = register.getType();
		this.category = new CategoryDTO(register.getCategory());
		this.person = new PersonDTO(register.getPerson());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
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

	public RegisterType getType() {
		return type;
	}

	public void setType(RegisterType type) {
		this.type = type;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	public PersonDTO getPerson() {
		return person;
	}

	public void setPerson(PersonDTO person) {
		this.person = person;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegisterDTO other = (RegisterDTO) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public String toString() {
		return "RegisterDTO [code=" + code + ", description=" + description + ", dueDate=" + dueDate + ", paymentDate="
				+ paymentDate + ", value=" + value + ", obs=" + obs + ", type=" + type + ", category=" + category
				+ ", person=" + person + "]";
	}
}
