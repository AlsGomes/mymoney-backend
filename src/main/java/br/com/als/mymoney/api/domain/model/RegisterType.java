package br.com.als.mymoney.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegisterType {
	INCOME("Receita"), EXPENSE("Despesa");

	private String text;
}
