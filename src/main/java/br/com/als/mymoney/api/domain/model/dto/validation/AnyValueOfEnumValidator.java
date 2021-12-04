package br.com.als.mymoney.api.domain.model.dto.validation;

import java.util.List;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnyValueOfEnumValidator implements ConstraintValidator<AnyValueOfEnum, CharSequence> {
	List<String> acceptedValues;

	@Override
	public void initialize(AnyValueOfEnum annotation) {
		ConstraintValidator.super.initialize(annotation);
		acceptedValues = Stream.of(annotation.enumClass().getEnumConstants()).map(Enum::name).toList();
	}

	@Override
	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {

		if (value == null)
			return false;

		if (value.isEmpty())
			return false;

		return acceptedValues.contains(value.toString());
	}

}
