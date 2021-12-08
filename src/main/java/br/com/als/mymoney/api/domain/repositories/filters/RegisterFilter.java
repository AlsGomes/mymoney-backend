package br.com.als.mymoney.api.domain.repositories.filters;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class RegisterFilter {

	private String description;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDateFrom;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDateUntil;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDueDateUntil() {
		return dueDateUntil;
	}

	public void setDueDateUntil(LocalDate dueDateUntil) {
		this.dueDateUntil = dueDateUntil;
	}

	public LocalDate getDueDateFrom() {
		return dueDateFrom;
	}

	public void setDueDateFrom(LocalDate dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
	}

	public boolean isValidDate() {
		if (dueDateFrom != null && dueDateUntil != null) {
			if (dueDateUntil.isBefore(dueDateFrom))
				return false;
		}

		return true;
	}
}
