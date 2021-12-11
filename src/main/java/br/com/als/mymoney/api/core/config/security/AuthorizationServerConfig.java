package br.com.als.mymoney.api.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@SuppressWarnings("deprecation")
@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;
    	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("angular")
					.secret(passwordEncoder.encode("@ngul@r0"))
					.scopes("read", "write")
					.authorizedGrantTypes("password", "refresh_token")					
					.accessTokenValiditySeconds(6 * 60 * 60) // 6 Hours
//					.accessTokenValiditySeconds(20) // 20 Seconds test
					.refreshTokenValiditySeconds(15 * 24 * 60 * 60) // 15 Days
				.and()
					.withClient("checktoken")
					.secret(passwordEncoder.encode("check123"));
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		var jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey("2f45ade2d9c22192ac38c66efbb19ac66a8f62f9822da769f979aa735711d4ac");
		return jwtAccessTokenConverter;
	}
	
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("isAuthenticated()");
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
        	.reuseRefreshTokens(false)
        	.accessTokenConverter(jwtAccessTokenConverter())
        	.userDetailsService(userDetailsService)
        	.authenticationManager(authenticationManager);
	}
}