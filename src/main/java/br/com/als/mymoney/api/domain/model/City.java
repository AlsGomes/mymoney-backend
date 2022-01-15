package br.com.als.mymoney.api.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "city")
public class City {

	@EqualsAndHashCode.Include
	@Id
	private Long id;

	private String name;

	@ManyToOne
	@JoinColumn(name = "id_state")
	private State state;

	private Integer ibge;

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", state=" + state + ", ibge=" + ibge + "]";
	}
}
