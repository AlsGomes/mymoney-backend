package br.com.als.mymoney.api.core.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.als.mymoney.api.core.config.properties.MyMoneyProperty;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private MyMoneyProperty properties;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
		.addMapping("/**")
		.allowedMethods("*")
		.allowedOrigins(properties.getSecurity().getAllowedOrigins())
		.allowCredentials(true)
		;
	}
}