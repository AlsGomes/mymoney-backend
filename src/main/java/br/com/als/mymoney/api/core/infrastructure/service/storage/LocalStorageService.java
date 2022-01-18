package br.com.als.mymoney.api.core.infrastructure.service.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import br.com.als.mymoney.api.core.config.properties.MyMoneyProperty;
import br.com.als.mymoney.api.domain.services.FileStorageService;

public class LocalStorageService implements FileStorageService {

	@Autowired
	private MyMoneyProperty properties;

	@Override
	public File save(FileStorageService.File file) {
		var fullPath = getRoot() + file.getFileName();
		var outputFile = new java.io.File(fullPath);
		if (!outputFile.getParentFile().exists())
			outputFile.getParentFile().mkdirs();

		try (OutputStream output = new FileOutputStream(outputFile)) {
			output.write(file.getInputStream().readAllBytes());
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			throw new StorageException("Erro ao tentar fazer upload de arquivo", e);
		}
	}

	@Override
	public File get(String registerCode, String fileName) {
		var fullPath = getRoot() + registerCode + "/" + fileName;

		try {
			var file = new java.io.File(fullPath);
			if (!file.exists())
				throw new FileNotFoundException();

			var name = file.getName();
			var size = file.length();
			var url = "file:///" + file.getPath().replace("\\", "/");
			var extension = name.substring(name.lastIndexOf(".")).toLowerCase();
			var contentType = "";

			switch (extension) {
			case ".pdf":
				contentType = MediaType.APPLICATION_PDF_VALUE;
				break;
			case ".png":
				contentType = MediaType.IMAGE_PNG_VALUE;
				break;
			case ".jpeg":
				contentType = MediaType.IMAGE_JPEG_VALUE;
				break;
			default:
				break;
			}				
			
			InputStream in;
			try  {
				in = new FileInputStream(file);
			} catch (IOException e) {
				e.printStackTrace();
				throw new StorageException(
						String.format("Erro ao buscar o arquivo %s, enquanto tentava tornar permanente", fullPath), e);
			}		

			return FileStorageService.File.builder()					
					.inputStream(in)
					.fileName(name)
					.size(size)
					.contentType(contentType)
					.url(url)
					.build();
		} catch (FileNotFoundException e) {
			throw new StorageException(String.format("Erro ao buscar o arquivo %s, enquanto tentava buscar", fullPath),
					e);
		}		
	}

	@Override
	public FileStorageService.File getTemporaryInfo(String fileName) {
		var fullPath = getRoot() + fileName;

		try {
			var file = new java.io.File(fullPath);
			if (!file.exists())
				throw new FileNotFoundException();

			var name = file.getName();
			var size = file.length();
			var extension = name.substring(name.lastIndexOf(".")).toLowerCase();
			var contentType = "";

			switch (extension) {
			case ".pdf":
				contentType = MediaType.APPLICATION_PDF_VALUE;
				break;
			case ".png":
				contentType = MediaType.IMAGE_PNG_VALUE;
				break;
			case ".jpeg":
				contentType = MediaType.IMAGE_JPEG_VALUE;
				break;
			default:
				break;
			}				

			return FileStorageService.File.builder()					
					.fileName(name)
					.size(size)
					.contentType(contentType)
					.build();
		} catch (FileNotFoundException e) {
			throw new StorageException(String.format("Erro ao buscar o arquivo %s, enquanto tentava buscar", fullPath),
					e);
		}
	}

	@Override
	public void deleteFiles(String registerCode) {
		var folder = new java.io.File(getRoot() + registerCode);
	
		if (!folder.exists())
			return;
		
		for (var file : folder.listFiles()) {
			file.delete();
		}

		folder.delete();
	}

	@Override
	public void deleteFile(String registerCode, String fileName) {
		var file = new java.io.File(getRoot() + registerCode + "/" + fileName);
	
		try {
			if (!file.exists())
				throw new FileNotFoundException();
		} catch (FileNotFoundException e) {
			throw new StorageException("O arquivo %s não existe", e);
		}
	
		file.delete();
	
		if (file.getParentFile().listFiles().length == 0) {
			file.getParentFile().delete();
		}
	}

	@Override
	public void deleteFileIfExists(String registerCode, String fileName) {
		var file = new java.io.File(getRoot() + registerCode + "/" + fileName);
	
		if (!file.exists())
			return;
	
		file.delete();
	
		if (file.getParentFile().listFiles().length == 0) {
			file.getParentFile().delete();
		}
	}

	@Override
	public void makePermanent(String registerCode, String fileName) {
		var fullPath = getRoot() + fileName;
		var tempFile = new java.io.File(fullPath);
	
		try {
			if (!tempFile.exists())
				throw new FileNotFoundException();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: ");
			e.printStackTrace();
			throw new StorageException(
					String.format("Erro ao buscar o arquivo %s, enquanto tentava tornar permanente", fullPath), e);
		}
	
		var outFolder = getRoot() + registerCode;
		try (InputStream in = new FileInputStream(tempFile)) {
			var outFile = new java.io.File(outFolder);
			if (!outFile.exists())
				outFile.mkdirs();
	
			try (OutputStream out = new FileOutputStream(outFolder + "/" + fileName)) {
				out.write(in.readAllBytes());
			} catch (IOException e) {
				e.printStackTrace();
				throw new StorageException(
						String.format("Erro ao buscar o arquivo %s, enquanto tentava tornar permanente", fullPath), e);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new StorageException(
					String.format("Erro ao buscar o arquivo %s, enquanto tentava tornar permanente", fullPath), e);
		}
	
		tempFile.delete();
	}
	
	private String getRoot() {
		return properties.getStorage().getLocal().getRoot() + "/";
	}
}
