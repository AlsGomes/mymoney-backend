package br.com.als.mymoney.api.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("mymoney")
@Component
@Getter
public class MyMoneyProperty {

	private final Security security = new Security();
	private final Mail mail = new Mail();

	@Setter
	@Getter
	public static class Security {
		private boolean enableHttps;
		private String allowedOrigins;
	}

	@Setter
	@Getter
	public static class Mail {
		private String from;
		private String host;
		private Integer port;
		private String username;
		private String password;
		private ImplType impl = ImplType.FAKE;

		public enum ImplType {
			FAKE, SMTP
		}
	}
}
