package br.com.als.mymoney.api.domain.model.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {
	private DataSize maxSize;

	@Override
	public void initialize(FileSize annotation) {
		ConstraintValidator.super.initialize(annotation);
		this.maxSize = DataSize.parse(annotation.max());
	}

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {		
		if (file == null)
			return true;		

		if (file.getSize() <= this.maxSize.toBytes())
			return true;
		
		return false;
	}
}
