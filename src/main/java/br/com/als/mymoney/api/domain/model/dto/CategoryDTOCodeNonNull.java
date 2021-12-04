package br.com.als.mymoney.api.domain.model.dto;

import javax.validation.constraints.NotBlank;

import br.com.als.mymoney.api.domain.model.Category;

public class CategoryDTOCodeNonNull {

	@NotBlank
	private String code;

	public CategoryDTOCodeNonNull() {
		super();
	}

	public CategoryDTOCodeNonNull(String code) {
		super();
		this.code = code;
	}

	public CategoryDTOCodeNonNull(Category category) {
		super();
		this.code = category.getCode();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
