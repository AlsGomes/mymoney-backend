package br.com.als.mymoney.api.domain.model.dto;

import br.com.als.mymoney.api.domain.model.State;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StateSummary {

	@EqualsAndHashCode.Include
	public Long id;

	public String name;
	public String initials;
	public Integer ibge;

	public StateSummary(State state) {
		this.id = state.getId();
		this.name = state.getName();
		this.initials = state.getInitials();
		this.ibge = state.getIbge();
	}

	@Override
	public String toString() {
		return "StateSummary [id=" + id + ", name=" + name + ", initials=" + initials + ", ibge=" + ibge + "]";
	}

}
