package br.com.als.mymoney.api.domain.model.dto;

import br.com.als.mymoney.api.domain.model.Category;

public class CategoryDTO {

	private String code;
	private String name;

	public CategoryDTO() {
		super();
	}

	public CategoryDTO(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	
	public CategoryDTO(Category category) {
		super();
		this.code = category.getCode();
		this.name = category.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
