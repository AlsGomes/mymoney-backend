package br.com.als.mymoney.api.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "person")
public class Person {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long id;

	@Getter
	private String code;

	@Getter
	@Setter
	private String name;

	@Setter
	private Boolean active;

	@Embedded
	@Getter
	@Setter
	private Address address;

	@Getter
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Contact> contacts = new ArrayList<>();

	public Person(Long id, String name, Boolean active, Address address) {
		this.id = id;
		this.name = name;
		this.active = active;
		this.address = address;
	}

	@PrePersist
	private void setCode() {
		this.code = UUID.randomUUID().toString();
	}

	public Boolean isActive() {
		return active;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", code=" + code + ", name=" + name + ", active=" + active + ", address=" + address
				+ ", contacts=" + contacts + "]";
	}
}
