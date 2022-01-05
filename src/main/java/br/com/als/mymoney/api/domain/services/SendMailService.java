package br.com.als.mymoney.api.domain.services;

import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

public interface SendMailService {

	void send(Message message);

	@Getter
	@Builder
	class Message {
		@Singular
		private Map<String, Object> params;

		@Singular
		private Set<String> recipients;

		private String subject;
		private String body;
	}
}
