package br.com.als.mymoney.api.domain.model.dto.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateTimeFormatWithPatternValidator implements ConstraintValidator<DateTimeFormatWithPattern, String> {

	private DateTimeFormatter dtf;
	private String pattern;

	@Override
	public void initialize(DateTimeFormatWithPattern annotation) {
		ConstraintValidator.super.initialize(annotation);
		this.pattern = annotation.pattern();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		try {
			this.dtf = DateTimeFormatter.ofPattern(pattern);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}

		try {
			if (value != null && !value.isEmpty())
				LocalDate.parse(value, dtf);

		} catch (DateTimeParseException e) {
			return false;
		}

		return true;
	}

}
