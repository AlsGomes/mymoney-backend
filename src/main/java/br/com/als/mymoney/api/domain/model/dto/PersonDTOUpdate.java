package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString
public class PersonDTOUpdate {

	@Getter
	private String code;

	@Getter
	@NotBlank
	@Size(min = 1, max = 50)
	private String name;
	
	private Boolean active;

	public Boolean isActive() {
		return active;
	}
}
