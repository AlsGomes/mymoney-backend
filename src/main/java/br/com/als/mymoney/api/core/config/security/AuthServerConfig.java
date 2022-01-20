package br.com.als.mymoney.api.core.config.security;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwsEncoder;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import br.com.als.mymoney.api.core.config.properties.MyMoneyProperty;

@Profile("oauth-security")
@Configuration
public class AuthServerConfig {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MyMoneyProperty properties;

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient angularClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("angular")
                .clientSecret(passwordEncoder.encode("@ngul@r0"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUris(uris -> uris.addAll(properties.getSecurity().getAllowedRedirects()))
                .scope("read")
                .scope("write")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(1)) // test
                        .refreshTokenTimeToLive(Duration.ofDays(15)) // test
//                        .accessTokenTimeToLive(Duration.ofHours(6))
//                        .refreshTokenTimeToLive(Duration.ofDays(15))
                        .build())
                .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(true)
                                .build())
                .build();

        RegisteredClient mobileClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("mobile")
                .clientSecret(passwordEncoder.encode("m0b1le"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUris(uris -> uris.addAll(properties.getSecurity().getAllowedRedirects()))
                .scope("read")
                .scope("write")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(6))
                        .refreshTokenTimeToLive(Duration.ofDays(15))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();

		return new InMemoryRegisteredClientRepository(
				Arrays.asList(angularClient, mobileClient));
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		return http.cors()
				.and()
					.formLogin(Customizer.withDefaults())
				.build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtBuildCustomizer() {
        return (context) -> {
            UsernamePasswordAuthenticationToken authenticationToken = context.getPrincipal();
            AppUser appUser = (AppUser) authenticationToken.getPrincipal();

            Set<String> authorities = new HashSet<>();
            for (GrantedAuthority grantedAuthority : appUser.getAuthorities()) {
                authorities.add(grantedAuthority.getAuthority());
            }

            context.getClaims().claim("name", appUser.getUser().getName());
            context.getClaims().claim("authorities", authorities);
        };
    }

    @Bean
    public JWKSet jwkSet() throws Exception {
		var keystorePath = properties.getSecurity().getKeystore().getPath();
		var keypairAlias = properties.getSecurity().getKeystore().getKeyPairAlias();
		var keyPass = properties.getSecurity().getKeystore().getKeyPassword();
		var storePass = properties.getSecurity().getKeystore().getStorePassword();

    	final InputStream inputStream = new ClassPathResource(keystorePath).getInputStream();

        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, storePass.toCharArray());

        RSAKey rsaKey = RSAKey.load(
                keyStore,
                keypairAlias,
                keyPass.toCharArray()
        );

        return new JWKSet(rsaKey);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) {
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwsEncoder(jwkSource);
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder()
                .issuer(properties.getSecurity().getAuthServerUrl())
                .build();
    }
}
