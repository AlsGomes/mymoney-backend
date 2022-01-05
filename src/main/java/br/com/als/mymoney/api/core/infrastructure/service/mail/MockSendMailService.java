package br.com.als.mymoney.api.core.infrastructure.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import br.com.als.mymoney.api.core.config.properties.MyMoneyProperty;
import br.com.als.mymoney.api.domain.services.SendMailService;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockSendMailService implements SendMailService {

	@Autowired
	private MyMoneyProperty properties;

	@Autowired
	private Configuration freeMarkerConfig;

	@Override
	public void send(Message message) {
		var body = processTemplate(message);
		log.info("Mail sent using property: {}", properties.getMail().getImpl());
		log.debug(body);
	}

	private String processTemplate(Message message) {
		try {
			var template = freeMarkerConfig.getTemplate(message.getBody());
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getParams());
		} catch (Exception e) {
			throw new MailException("Error processing mail template", e);
		}
	}
}
