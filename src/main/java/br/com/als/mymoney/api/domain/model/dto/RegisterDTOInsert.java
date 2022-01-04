package br.com.als.mymoney.api.domain.model.dto;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.als.mymoney.api.domain.model.RegisterType;
import br.com.als.mymoney.api.domain.model.dto.validation.AnyValueOfEnum;
import br.com.als.mymoney.api.domain.model.dto.validation.DateTimeFormatWithPattern;
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
@JsonInclude(Include.NON_NULL)
public class RegisterDTOInsert {

	@NotBlank
	@Size(min = 1, max = 50)
	private String description;

	@DateTimeFormatWithPattern(pattern = "yyyy-MM-dd")
	@NotBlank
	private String dueDate;

	@DateTimeFormatWithPattern(pattern = "yyyy-MM-dd")
	private String paymentDate;

	@NotNull
	@DecimalMin(value = "0.00")
	private BigDecimal value;

	@Size(min = 1, max = 100)
	private String obs;

	@NotBlank
	@AnyValueOfEnum(enumClass = RegisterType.class)
	private String type;

	@NotNull
	@Valid
	private CategoryDTOCodeNonNull category;

	@NotNull
	@Valid
	private PersonDTOCodeNonNull person;
}
