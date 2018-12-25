package com.demo.config;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.model.Account;
import com.demo.model.AccountRole;
import com.demo.service.AccountService;

@Configuration
public class AppConfig {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {
			@Autowired
			AccountService accountService;

			@Override
			public void run(ApplicationArguments args) throws Exception {
				Set roleSet = new HashSet();
				roleSet.add(AccountRole.ADMIN);
				roleSet.add(AccountRole.USER);
				Account dongha = Account.builder()
						.email("shindh159@gmail.com")
						.password("1234")
						.roles(roleSet)
						.build();
				accountService.saveAccount(dongha);
			}
		};
	}
}
