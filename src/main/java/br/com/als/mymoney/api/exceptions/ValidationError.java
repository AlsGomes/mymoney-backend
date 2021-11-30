package br.com.als.mymoney.api.exceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

	private List<FieldError> errors = new ArrayList<>();

	public ValidationError(Integer status, String type, String title, String detail, String userDetail,
			LocalDateTime dateTime, List<FieldError> errors) {
		super(status, type, title, detail, userDetail, dateTime);
		this.errors = errors;
	}

	public void addError(String fieldName, String message) {
		errors.add(new FieldError(fieldName, message));
	}

	public List<FieldError> getErrors() {
		return errors;
	}

	public void setErrors(List<FieldError> errors) {
		this.errors = errors;
	}

	public static class FieldError {
		private String fieldName;
		private String message;

		public FieldError(String fieldName, String message) {
			this.fieldName = fieldName;
			this.message = message;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
