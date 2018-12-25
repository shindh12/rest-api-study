package com.demo.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.model.Account;
import com.demo.model.AccountRole;
import com.demo.repo.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	public void findByUsername() {
		//Given
		Set roleSet = new HashSet();
		roleSet.add(AccountRole.ADMIN);
		roleSet.add(AccountRole.USER);
		String password = "1234";
		String email = "shindh159@gmail.com";
		Account account = Account.builder()
				.email(email)
				.password(password)
				.roles(roleSet)
				.build();
//		accountRepository.save(account);
		accountService.saveAccount(account);

		//When
		UserDetailsService userDetailsService = (UserDetailsService) accountService;
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		
		//Then
		assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
	}
	
//	@Test(expected = UsernameNotFoundException.class)
	@Test
	public void findByUsernameFail() {
		String email = "fail@gmail.com";
//		1. expected Annotation으로 테스트
//		accountService.loadUserByUsername(email);

//		2. try~catch 로 예외 테스트
//		try {
//			accountService.loadUserByUsername(email);
//			fail("supposed to be failed");
//		} catch (UsernameNotFoundException e) {
//			assertThat(e.getMessage()).containsSequence(email);
//		}
//		3. expectedException rule로 예외 테스트

		//Expected -> 먼저 예측 후 테스트
		expectedException.expect(UsernameNotFoundException.class);
		expectedException.expectMessage(Matchers.containsString(email));
		
		// When
		accountService.loadUserByUsername(email);
	}
}

