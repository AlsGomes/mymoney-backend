package br.com.als.mymoney.api.domain.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "contact")
public class Contact {

	@Setter
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;

	@Setter
	private String name;

	@Setter
	private String email;

	@Setter
	private String telephone;

	@Setter
	@ManyToOne
	@JoinColumn(name = "id_person")
	private Person person;

	public Contact(Long id, String name, String email, String telephone, Person person) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.telephone = telephone;
		this.person = person;
	}

	@PrePersist
	private void setCode() {
		this.code = UUID.randomUUID().toString();
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", code=" + code + ", name=" + name + ", email=" + email + ", telephone="
				+ telephone + "]";
	}
}
