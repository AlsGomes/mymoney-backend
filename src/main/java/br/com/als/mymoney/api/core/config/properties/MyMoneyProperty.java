package br.com.als.mymoney.api.core.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.amazonaws.regions.Regions;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("mymoney")
@Component
@Getter
public class MyMoneyProperty {

	private final Security security = new Security();
	private final Mail mail = new Mail();
	private final Storage storage = new Storage();

	@Setter
	@Getter
	public static class Security {
		private Keystore keystore = new Keystore();

		private List<String> allowedOrigins;
		private List<String> allowedRedirects;
		private String authServerUrl;

		@Getter
		@Setter
		public static class Keystore {
			private String path;
			private String keyPassword;
			private String storePassword;
			private String keyPairAlias;
		}
	}

	@Setter
	@Getter
	public static class Mail {
		private String from;
		private String host;
		private Integer port;
		private String username;
		private String password;
		private ImplType impl = ImplType.FAKE;

		public enum ImplType {
			FAKE, SMTP
		}
	}

	@Setter
	@Getter
	public static class Storage {
		private ImplType impl = ImplType.LOCAL;

		private final S3 s3 = new S3();
		private final Local local = new Local();

		public enum ImplType {
			S3, LOCAL
		}

		@Getter
		@Setter
		public static class S3 {
			private String accessKeyId;
			private String secretAccessKey;
			private String bucket;
			private Regions region;
			private String root;
		}

		@Getter
		@Setter
		public static class Local {
			private String root;
		}
	}

}
