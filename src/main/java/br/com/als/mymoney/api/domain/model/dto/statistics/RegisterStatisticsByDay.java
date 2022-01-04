package br.com.als.mymoney.api.domain.model.dto.statistics;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.als.mymoney.api.domain.model.RegisterType;
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
public class RegisterStatisticsByDay {
	private RegisterType type;
	private LocalDate day;
	private BigDecimal total;
}
