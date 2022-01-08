package br.com.als.mymoney.api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "registers")
public class Register {

	@Setter
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;

	@Setter
	private String description;

	@Setter
	private LocalDate dueDate;

	@Setter
	private LocalDate paymentDate;

	@Setter
	private BigDecimal value;

	@Setter
	private String obs;

	@Setter
	@Enumerated(EnumType.STRING)
	private RegisterType type;

	@Setter
	@ManyToOne
	@JoinColumn(name = "id_category")
	private Category category;

	@Setter
	@ManyToOne
	@JoinColumn(name = "id_person")
	private Person person;

	@Setter
	@OneToMany(mappedBy = "register", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RegisterFile> files;

	@PrePersist
	private void setCode() {
		this.code = UUID.randomUUID().toString();
	}
}
