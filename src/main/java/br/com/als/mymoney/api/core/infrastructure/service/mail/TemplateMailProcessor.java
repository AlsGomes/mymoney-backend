package br.com.als.mymoney.api.core.infrastructure.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import br.com.als.mymoney.api.domain.services.SendMailService.Message;
import freemarker.template.Configuration;

@Component
public class TemplateMailProcessor {

	@Autowired
	private Configuration freeMarkerConfig;

	public String processTemplate(Message message) {
		try {
			var template = freeMarkerConfig.getTemplate(message.getBody());
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getParams());
		} catch (Exception e) {
			throw new MailException("Error processing mail template", e);
		}
	}
}
