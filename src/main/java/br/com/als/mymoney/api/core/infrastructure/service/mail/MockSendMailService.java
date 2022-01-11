package br.com.als.mymoney.api.core.infrastructure.service.mail;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.als.mymoney.api.core.config.properties.MyMoneyProperty;
import br.com.als.mymoney.api.domain.services.SendMailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockSendMailService implements SendMailService {

	@Autowired
	private MyMoneyProperty properties;

	@Autowired
	private TemplateMailProcessor templateMailProcessor;

	@Override
	public void send(Message message) {
		var body = templateMailProcessor.processTemplate(message);
		log.info("Mail sent using property: {}", properties.getMail().getImpl());
		log.debug(body);
	}

}
