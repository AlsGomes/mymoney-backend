package br.com.als.mymoney.api.core.infrastructure.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import br.com.als.mymoney.api.core.config.properties.MyMoneyProperty;
import br.com.als.mymoney.api.domain.services.SendMailService;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmtpSendMailService implements SendMailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MyMoneyProperty properties;

	@Autowired
	private Configuration freeMarkerConfig;

	@Override
	public void send(Message message) {
		try {
			var body = processTemplate(message);
			var mimeMessage = mailSender.createMimeMessage();
			var helper = new MimeMessageHelper(mimeMessage, "UTF-8");

			helper.setSubject(message.getSubject());
			helper.setText(body, true);
			helper.setTo(message.getRecipients().toArray(new String[0]));
			helper.setFrom(properties.getMail().getFrom());

			mailSender.send(mimeMessage);

			log.info("Mail sent using property: {}", properties.getMail().getImpl());
		} catch (Exception e) {
			throw new MailException("Error sending Smtp Email", e);
		}
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
