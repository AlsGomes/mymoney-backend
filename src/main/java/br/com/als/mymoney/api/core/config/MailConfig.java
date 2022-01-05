package br.com.als.mymoney.api.core.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import br.com.als.mymoney.api.core.config.properties.MyMoneyProperty;
import br.com.als.mymoney.api.core.infrastructure.service.mail.MockSendMailService;
import br.com.als.mymoney.api.core.infrastructure.service.mail.SmtpSendMailService;
import br.com.als.mymoney.api.domain.services.SendMailService;

@Configuration
public class MailConfig {

	@Autowired
	private MyMoneyProperty properties;

	@Bean
	public JavaMailSender javaMailSender() {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.connectiontimeout", 10 * 1000);

		var mailSender = new JavaMailSenderImpl();
		mailSender.setJavaMailProperties(props);
		mailSender.setHost(properties.getMail().getHost());
		mailSender.setPort(properties.getMail().getPort());
		mailSender.setUsername(properties.getMail().getUsername());
		mailSender.setPassword(properties.getMail().getPassword());

		return mailSender;
	}

	@Bean
	public SendMailService sendMailService() {
		switch (properties.getMail().getImpl()) {
		case FAKE:
			return new MockSendMailService();
		case SMTP:
			return new SmtpSendMailService();
		default:
			return null;
		}
	}
}
