package br.com.als.mymoney.api.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "country")
public class Country {

	@EqualsAndHashCode.Include
	@Id
	private Long id;

	@Column(name = "name_pt_br")
	private String namePtBr;

	private String name;
	private String initials;
	private Integer bacen;

	@Override
	public String toString() {
		return "Country [id=" + id + ", namePtBr=" + namePtBr + ", name=" + name + ", initials=" + initials + ", bacen="
				+ bacen + "]";
	}
}
