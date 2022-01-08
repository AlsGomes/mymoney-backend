package br.com.als.mymoney.api.domain.model.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullButNotEmptyValidator implements ConstraintValidator<NullButNotEmpty, String> {

	@Override
	public void initialize(NullButNotEmpty annotation) {
		ConstraintValidator.super.initialize(annotation);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return true;

		if (!value.isBlank())
			return true;

		return false;
	}
}
