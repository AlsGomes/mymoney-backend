package br.com.als.mymoney.api.domain.services;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

public interface FileStorageService {

	File save(File file);

	File get(String registerCode, String fileName);

	File getTemporaryInfo(String fileName);
	
	void deleteFile(String registerCode, String fileName);

	void deleteFileIfExists(String registerCode, String fileName);

	void deleteFiles(String registerCode);

	void makePermanent(String registerCode, String fileName);
	
	default String generateName(String originalFileName) {
		return UUID.randomUUID().toString() + "_" + originalFileName;
	}

	@Getter
	@Builder
	class File {
		private InputStream inputStream;
		private String fileName;
		private long size;
		private String contentType;
	}
}
