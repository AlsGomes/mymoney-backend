package br.com.als.mymoney.api.exceptions;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.als.mymoney.api.core.infrastructure.service.storage.StorageException;
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
	
	@ExceptionHandler(StorageException.class)
	public ResponseEntity<?> handleStorageException(StorageException ex, WebRequest request) {
		var status = HttpStatus.CONFLICT;
		
		StandardError error = getStandardErrorInfo(
				status,
				"storage-exception",
				ex.getMessage(),
				"Não foi possível processar sua solicitação");
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<?> handleViolationConstraintException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
		var status = HttpStatus.CONFLICT;
		
		StandardError error = getStandardErrorInfo(
				status,
				"violation-constraint-exception",
				ex.getMessage(),
				"O recurso solicitado para exclusão/alteração não pode ser executado, pois, já está em uso");
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}	
			
	@ExceptionHandler(SizeLimitExceededException.class)
	public ResponseEntity<Object> handleSizeLimitExceeded(SizeLimitExceededException ex) {		
		var status = HttpStatus.PAYLOAD_TOO_LARGE;
		
		StandardError error = getStandardErrorInfo(
				status,
				"size-limit-exceeded", 
				ex.getMessage(),
				String.format("O limite de tamanho da requisição foi excedido. Tamanho atual: %s bytes Tamanho máximo: %s bytes",
						ex.getActualSize(), ex.getPermittedSize()));

		return handleExceptionInternal(ex, error, new HttpHeaders(), status, null);
	}
	
	@ExceptionHandler(FileSizeLimitExceededException.class)
	public ResponseEntity<Object> handleSizeLimitExceeded(FileSizeLimitExceededException ex) {		
		var status = HttpStatus.BAD_REQUEST;
		
		StandardError error = getStandardErrorInfo(
				status,
				"file-size-limit-exceeded", 
				"The field file exceeds its maximum permitted size",
				"O limite de tamanho do arquivo foi excedido");
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, null);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		StandardError error = getStandardErrorInfo(
				status,
				"not-acceptable-media-type", 
				ex.getMessage(),
				"O tipo de mídia não é suportado");
		
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
	protected ResponseEntity<Object> handleBindException(BindException ex,
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
