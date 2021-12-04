package br.com.als.mymoney.api.exceptions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.als.mymoney.api.domain.services.exceptions.DomainException;
import br.com.als.mymoney.api.domain.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<?> handleObjectNotFound(ObjectNotFoundException ex, WebRequest request) {
		var status = HttpStatus.NOT_FOUND;
		
		StandardError error = getStandardErrorInfo(
				status,
				"object-not-found-exception",
				ex.getMessage(),
				"O recurso buscado não existe");

		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}	

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<?> handleDomainException(DomainException ex, WebRequest request) {
		var status = HttpStatus.UNPROCESSABLE_ENTITY;
				
		StandardError error = getStandardErrorInfo(
				status,
				"domain-exception",
				ex.getMessage(),
				"Não foi possível processar sua solicitação");
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}	
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		return handleExceptionInternal(
				ex, 
				"Not readable message. Verify if there's some attributes that doesn't exists", 
				new HttpHeaders(),
				status, 
				request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		BindingResult bindingResult = ex.getBindingResult();
		List<ValidationError.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
				.map((fieldError)-> {
					String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
					
					return new ValidationError.FieldError(
							fieldError.getField(),
							message);
					})
				.collect(Collectors.toList());
		
		ValidationError error = new ValidationError(
				status.value(),
				null,
				status.getReasonPhrase(),
				"One or more fields are invalid","Um ou mais campos estão inválidos",
				LocalDateTime.now(),
				fieldErrors);

		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) { 
				
		if (body == null) {
			body = getStandardErrorInfo(status, null, "Error ocurred", null);
		} else if(body instanceof String) {
			body = getStandardErrorInfo(status, null, ((String) body), null);
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private StandardError getStandardErrorInfo(HttpStatus status, String path, String errorMessage, String userDetail) {		
		var type = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{type}")
				.buildAndExpand(path)
				.toUri();
		
		return new StandardError(
				status.value(), 
				type.toString(), 
				status.getReasonPhrase(), 
				errorMessage,
				userDetail, 
				LocalDateTime.now());
	}
}
