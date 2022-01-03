package br.com.als.mymoney.api.domain.model.dto.statistics;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import br.com.als.mymoney.api.domain.model.Category;
import br.com.als.mymoney.api.domain.model.dto.CategoryDTO;

public class RegisterStatisticsByCategory {
	private CategoryDTO category;
	private BigDecimal total;

	public RegisterStatisticsByCategory(Category category, BigDecimal total) {
		this.category = new CategoryDTO();
		BeanUtils.copyProperties(category, this.category);

		this.total = total;
	}

	public RegisterStatisticsByCategory(CategoryDTO category, BigDecimal total) {
		this.category = category;
		this.total = total;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
