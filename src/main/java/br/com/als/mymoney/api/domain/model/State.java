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
@Table(name = "state")
public class State {

	@EqualsAndHashCode.Include
	@Id
	public Long id;

	public String name;
	public String initials;
	public Integer ibge;
	public String ddd;

	@ManyToOne
	@JoinColumn(name = "id_country")
	public Country country;

	@Override
	public String toString() {
		return "State [id=" + id + ", name=" + name + ", initials=" + initials + ", ibge=" + ibge + ", ddd=" + ddd
				+ ", country=" + country + "]";
	}
}
