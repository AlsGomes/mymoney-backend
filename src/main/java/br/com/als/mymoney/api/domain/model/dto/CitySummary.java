package br.com.als.mymoney.api.domain.model.dto;

import br.com.als.mymoney.api.domain.model.City;
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
public class CitySummary {

	@EqualsAndHashCode.Include
	private Long id;

	private String name;
	private Integer ibge;
	private Long stateId;

	public CitySummary(City city) {
		this.id = city.getId();
		this.name = city.getName();
		this.ibge = city.getIbge();
		this.stateId = city.getState().getId();
	}

}
