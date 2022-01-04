package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.constraints.NotBlank;

import br.com.als.mymoney.api.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class CategoryDTOCodeNonNull {

	@EqualsAndHashCode.Include
	@NotBlank
	private String code;

	public CategoryDTOCodeNonNull(Category category) {
		this.code = category.getCode();
	}
}
