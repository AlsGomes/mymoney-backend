package br.com.als.mymoney.api.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("mymoney")
@Component
public class MyMoneyProperty {

	private final Security security = new Security();

	public Security getSecurity() {
		return security;
	}

	public static class Security {
		private boolean enableHttps;
		private String allowedOrigins;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}

		public String getAllowedOrigins() {
			return allowedOrigins;
		}

		public void setAllowedOrigins(String allowedOrigins) {
			this.allowedOrigins = allowedOrigins;
		}
	}
}
