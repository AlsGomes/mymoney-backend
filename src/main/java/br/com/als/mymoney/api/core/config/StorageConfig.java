package br.com.als.mymoney.api.core.config;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;

import br.com.als.mymoney.api.core.config.properties.MyMoneyProperty;
import br.com.als.mymoney.api.core.infrastructure.service.storage.LocalStorageService;
import br.com.als.mymoney.api.core.infrastructure.service.storage.S3StorageService;
import br.com.als.mymoney.api.domain.services.FileStorageService;

@Configuration
public class StorageConfig {

	@Autowired
	private MyMoneyProperty properties;

	@Bean
	public AmazonS3 amazonS3() {
		var bucket = properties.getStorage().getS3().getBucket();
		var accessKeyId = properties.getStorage().getS3().getAccessKeyId();
		var secretAccessKey = properties.getStorage().getS3().getSecretAccessKey();
		var region = properties.getStorage().getS3().getRegion();
		var credentials = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey));

		var amazonS3 = AmazonS3ClientBuilder
				.standard()
				.withCredentials(credentials)
				.withRegion(region)
				.build();
				
		if (!amazonS3.doesBucketExistV2(bucket)) {
			amazonS3.createBucket(new CreateBucketRequest(bucket));
			
			var bucketLifecycleConfigRule = new BucketLifecycleConfiguration.Rule()
					.withId("Temporary files expiration")
					.withFilter(new LifecycleFilter(new LifecycleTagPredicate(new Tag("expire", "true"))))
					.withExpirationInDays(1)
					.withStatus(BucketLifecycleConfiguration.ENABLED);
			
			var bucketLifecycleConfig = new BucketLifecycleConfiguration()
					.withRules(bucketLifecycleConfigRule);
			
			amazonS3.setBucketLifecycleConfiguration(bucket, bucketLifecycleConfig);
		}
		
		return amazonS3;
	}

	@Bean
	public FileStorageService fileStorageService() {
		switch (properties.getStorage().getImpl()) {
		case LOCAL:
			return new LocalStorageService();
		case S3:
			return new S3StorageService();
		default:
			throw new NotImplementedException("application.properties has inavlid implementation for storage service");
		}
	}
}
