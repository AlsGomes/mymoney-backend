package br.com.als.mymoney.api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "registers")
public class Register {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;
	private String description;
	private LocalDate dueDate;
	private LocalDate paymentDate;
	private BigDecimal value;
	private String obs;

	@Enumerated(EnumType.STRING)
	private RegisterType type;

	@ManyToOne
	@JoinColumn(name = "id_category")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "id_person")
	private Person person;

	public Register() {
		super();
	}

	public Register(
			Long id, 
			String code, 
			String description, 
			LocalDate dueDate, 
			LocalDate paymentDate,
			BigDecimal value, 
			String obs, 
			RegisterType type, 
			Category category, 
			Person person) {
		super();
		this.id = id;
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	@PrePersist
	private void setCode() {
		this.code = UUID.randomUUID().toString();
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Register other = (Register) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Register [id=" + id + ", code=" + code + ", description=" + description + ", dueDate=" + dueDate
				+ ", paymentDate=" + paymentDate + ", value=" + value + ", obs=" + obs + ", type=" + type
				+ ", category=" + category + ", person=" + person + "]";
	}
}
