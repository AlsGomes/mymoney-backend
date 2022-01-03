package br.com.als.mymoney.api.domain.model.dto.statistics;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.als.mymoney.api.domain.model.RegisterType;

public class RegisterStatisticsByDay {
	private RegisterType type;
	private LocalDate day;
	private BigDecimal total;

	public RegisterStatisticsByDay(RegisterType type, LocalDate day, BigDecimal total) {
		this.type = type;
		this.day = day;
		this.total = total;
	}

	public RegisterType getType() {
		return type;
	}

	public void setType(RegisterType type) {
		this.type = type;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
