package br.com.als.mymoney.api.domain.model.dto.statistics;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import br.com.als.mymoney.api.domain.model.Category;
import br.com.als.mymoney.api.domain.model.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class RegisterStatisticsByCategory {
	private CategoryDTO category;
	private BigDecimal total;

	public RegisterStatisticsByCategory(Category category, BigDecimal total) {
		this.category = new CategoryDTO();
		BeanUtils.copyProperties(category, this.category);

		this.total = total;
	}
}
