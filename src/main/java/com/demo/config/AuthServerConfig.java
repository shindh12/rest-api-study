package com.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.demo.service.AccountService;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter{
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AccountService accountService;
	
	@Autowired
	TokenStore tokenStore;
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		유저 정보 - auth manager, tokenstore, userdetails 를 등록
		endpoints.authenticationManager(authenticationManager)
					.userDetailsService(accountService)
					.tokenStore(tokenStore);
	}
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		passwordEncoder를 이용해서 관리하도록 설정
		security.passwordEncoder(passwordEncoder);
	}
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		나중에 jdbc로 설정하고 현재는 inmemory로 클라이언트 등록
		clients.inMemory()
			.withClient("client")
			.authorizedGrantTypes("password", "refresh_token")
			.scopes("read", "write")
			.secret(passwordEncoder.encode("secret"))
			.accessTokenValiditySeconds(10*60)
			.refreshTokenValiditySeconds(6 * 10 * 60);
	}
}
