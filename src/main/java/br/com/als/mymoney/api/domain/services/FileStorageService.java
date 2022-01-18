package br.com.als.mymoney.api.domain.services;

import java.io.InputStream;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
	@JsonInclude(Include.NON_NULL)
	class File {		
		private String fileName;
		private long size;
		private String contentType;
		private String url;

		@Setter
		private InputStream inputStream;

		public boolean hasBytes() {
			return this.inputStream != null;
		}

		public boolean hasUrl() {
			return this.url != null;
		}

		@Override
		public String toString() {
			return "File [fileName=" + fileName + ", size=" + size + ", contentType=" + contentType + ", url=" + url
					+ "]";
		}
	}
}
