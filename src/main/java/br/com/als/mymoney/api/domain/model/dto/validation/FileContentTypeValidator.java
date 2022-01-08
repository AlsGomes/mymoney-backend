package br.com.als.mymoney.api.domain.model.dto.validation;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

	private String[] allowed;

	@Override
	public void initialize(FileContentType annotation) {
		ConstraintValidator.super.initialize(annotation);
		this.allowed = annotation.allowed();
	}

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		if (file == null)
			return true;

		if (Arrays.asList(this.allowed).contains(file.getContentType()))
			return true;

		return false;
	}
}
