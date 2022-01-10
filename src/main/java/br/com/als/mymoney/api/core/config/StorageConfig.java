package br.com.als.mymoney.api.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.als.mymoney.api.core.infrastructure.service.storage.LocalStorageService;
import br.com.als.mymoney.api.domain.services.FileStorageService;

@Configuration
public class StorageConfig {

	@Bean
	public FileStorageService fileStorageService() {
		return new LocalStorageService();
	}
}
