package br.com.als.mymoney.api.domain.model;

public enum RegisterType {

	INCOME("Receita"), EXPENSE("Despesa");

	private String text;

	private RegisterType(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
