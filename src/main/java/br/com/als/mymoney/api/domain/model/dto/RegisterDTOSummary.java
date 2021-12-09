package br.com.als.mymoney.api.domain.model.dto;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.als.mymoney.api.domain.model.Register;
import br.com.als.mymoney.api.domain.model.RegisterType;

@JsonInclude(Include.NON_NULL)
public class RegisterDTOSummary {

	private String code;
	private String description;
	private String dueDate;
	private String paymentDate;
	private BigDecimal value;
	private String obs;
	private RegisterType type;
	private CategoryDTO category;
	private PersonDTOSummary person;

	public RegisterDTOSummary() {
	}

	public RegisterDTOSummary(
			String code, 
			String description, 
			String dueDate, 
			String paymentDate, 
			BigDecimal value,
			String obs, 
			RegisterType type, 
			CategoryDTO category, 
			PersonDTOSummary person) {		
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

	public RegisterDTOSummary(Register register) {
		super();
		this.code = register.getCode();
		this.description = register.getDescription();
		this.dueDate = register.getDueDate().toString();
		this.paymentDate = register.getPaymentDate() == null ? null : register.getPaymentDate().toString();
		this.value = register.getValue();
		this.obs = register.getObs();
		this.type = register.getType();
		this.category = new CategoryDTO(register.getCategory());
		this.person = new PersonDTOSummary(register.getPerson());
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

	public PersonDTOSummary getPerson() {
		return person;
	}

	public void setPerson(PersonDTOSummary person) {
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
		RegisterDTOSummary other = (RegisterDTOSummary) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public String toString() {
		return "RegisterDTOSummary [code=" + code + ", description=" + description + ", dueDate=" + dueDate + ", paymentDate="
				+ paymentDate + ", value=" + value + ", obs=" + obs + ", type=" + type + ", category=" + category
				+ ", person=" + person + "]";
	}
}
