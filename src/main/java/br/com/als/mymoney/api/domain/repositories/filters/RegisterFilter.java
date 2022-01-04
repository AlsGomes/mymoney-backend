package br.com.als.mymoney.api.domain.repositories.filters;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterFilter {

	private String description;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDateFrom;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDateUntil;

	public boolean isValidDate() {
		if (dueDateFrom != null && dueDateUntil != null) {
			if (dueDateUntil.isBefore(dueDateFrom))
				return false;
		}

		return true;
	}
}
