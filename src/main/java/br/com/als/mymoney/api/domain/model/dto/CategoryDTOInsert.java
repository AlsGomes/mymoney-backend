package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CategoryDTOInsert {

	@NotBlank
	@Size(min = 1, max = 50)
	private String name;

	public CategoryDTOInsert() {
		super();
	}

	public CategoryDTOInsert(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
