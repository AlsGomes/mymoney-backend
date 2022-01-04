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

	@Setter
	@Getter
	public static class Security {
		private boolean enableHttps;
		private String allowedOrigins;
	}
}
