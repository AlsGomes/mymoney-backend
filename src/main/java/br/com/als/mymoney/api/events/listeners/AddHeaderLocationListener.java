package br.com.als.mymoney.api.events.listeners;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.als.mymoney.api.events.ResourceCreatedEvent;

@Component
public class AddHeaderLocationListener implements ApplicationListener<ResourceCreatedEvent> {

	@Override
	public void onApplicationEvent(ResourceCreatedEvent event) {
		HttpServletResponse response = event.getResponse();		
		addHeaderLocation(response, event.getCode());
	}
	
	private void addHeaderLocation(HttpServletResponse response, String code) {
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{code}")
				.buildAndExpand(code)
				.toUri();
		
		response.addHeader(HttpHeaders.LOCATION, uri.toString());
	}
}
