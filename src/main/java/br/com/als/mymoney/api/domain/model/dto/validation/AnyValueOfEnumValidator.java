package br.com.als.mymoney.api.domain.model.dto.validation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnyValueOfEnumValidator implements ConstraintValidator<AnyValueOfEnum, String> {
	List<String> acceptedValues;

	@Override
	public void initialize(AnyValueOfEnum annotation) {
		ConstraintValidator.super.initialize(annotation);
		acceptedValues = Arrays.asList(annotation.enumClass().getEnumConstants()).stream().map(Enum::name)
				.collect(Collectors.toList());

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (value == null)
			return false;

		if (value.isEmpty())
			return false;

		return acceptedValues.contains(value.toString());
	}

}
