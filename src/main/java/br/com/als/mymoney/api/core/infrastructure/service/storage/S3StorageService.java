package br.com.als.mymoney.api.core.infrastructure.service.storage;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;

import br.com.als.mymoney.api.core.config.properties.MyMoneyProperty;
import br.com.als.mymoney.api.domain.services.FileStorageService;

public class S3StorageService implements FileStorageService {

	@Autowired
	private AmazonS3 amazonS3;

	@Autowired
	private MyMoneyProperty properties;

	@Override
	public File save(File file) {
		try {						
			var fileName = getPath(file.getFileName());
			var contentType = file.getContentType();
			var size = file.getSize();
			var fileInputStream = file.getInputStream();
			
			var objectMetaData = new ObjectMetadata();
			objectMetaData.setContentType(contentType);
			objectMetaData.setContentLength(size);
			
			var putObjectRequest = new PutObjectRequest(
					getBucket(), 
					fileName,
					fileInputStream,
					objectMetaData)
				.withCannedAcl(CannedAccessControlList.PublicRead);			
			
			putObjectRequest.setTagging(new ObjectTagging(Arrays.asList(new Tag("expire", "true"))));

			amazonS3.putObject(putObjectRequest);

			return file;
		} catch (Exception e) {
			e.printStackTrace();
			throw new StorageException("Erro ao tentar salvar arquivo no S3", e);
		}
	}

	@Override
	public File get(String registerCode, String fileName) {
		return getInfo(registerCode, fileName);
	}

	@Override
	public File getTemporaryInfo(String fileName) {
		var getObjectMetadataReq = new GetObjectMetadataRequest(getBucket(), getPath(fileName));
		var metadata = amazonS3.getObjectMetadata(getObjectMetadataReq);		
		return File.builder()
				.contentType(metadata.getContentType())
				.size(metadata.getContentLength())
				.fileName(fileName)				
				.build();
	}

	@Override
	public void deleteFile(String registerCode, String fileName) {
		var deleteObjectRequest = new DeleteObjectRequest(
				getBucket(),
				getPath(String.format("%s/%s", registerCode, fileName)));

		amazonS3.deleteObject(deleteObjectRequest);
	}

	@Override
	public void deleteFileIfExists(String registerCode, String fileName) {
		deleteFile(registerCode, fileName);
	}

	@Override
	public void deleteFiles(String registerCode) {
		var listObjectRequest = new ListObjectsRequest();
		listObjectRequest.setBucketName(getBucket());
		listObjectRequest.setPrefix(getPath(registerCode));

		var list = amazonS3.listObjects(listObjectRequest);
		list.getObjectSummaries().forEach(obj -> {
			var path = obj.getKey().split("/");
			deleteFile(registerCode, path[path.length - 1]);
		});
	}

	@Override
	public void makePermanent(String registerCode, String fileName) {
		var permanentFilePath = copyBucketObject(registerCode, fileName);

		var setObjectTaggingRequest = new SetObjectTaggingRequest(
				getBucket(), 
				permanentFilePath,
				new ObjectTagging(Collections.emptyList()));

		amazonS3.setObjectTagging(setObjectTaggingRequest);
	}

	private String getPath(String fileName) {
		return properties.getStorage().getS3().getRoot() + "/" + fileName;
	}
	
	private String getBucket() {
		return properties.getStorage().getS3().getBucket();
	}
	
	private String copyBucketObject(String registerCode, String fileName) {
		var destination = getPath(registerCode + "/" + fileName);
		
		var copyReq = new CopyObjectRequest(
				getBucket(), 
				getPath(fileName), 
				getBucket(), 
				destination)
			.withCannedAccessControlList(CannedAccessControlList.PublicRead);
		
		amazonS3.copyObject(copyReq);		
		return destination;
	}
		
	private File getInfo(String registerCode, String fileName) {
		var fullPath = getPath(String.format("%s/%s", registerCode, fileName));
		var getObjectMetadataReq = new GetObjectMetadataRequest(getBucket(), fullPath);		
		var metadata = amazonS3.getObjectMetadata(getObjectMetadataReq);
		var url = amazonS3.getUrl(getBucket(), fullPath);
		
		return File.builder()
				.contentType(metadata.getContentType())
				.size(metadata.getContentLength())
				.fileName(fileName)		
				.url(url.toString())
				.build();
	}
}
