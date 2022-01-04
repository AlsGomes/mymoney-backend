package br.com.als.mymoney.api.domain.model.dto;

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
public class CategoryDTO {

	@EqualsAndHashCode.Include
	private String code;
	
	private String name;

	public CategoryDTO(Category category) {		
		this.code = category.getCode();
		this.name = category.getName();
	}
}
