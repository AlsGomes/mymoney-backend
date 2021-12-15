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

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
	}
}