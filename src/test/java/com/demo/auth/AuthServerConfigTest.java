package com.demo.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo.common.BaseControllerTest;
import com.demo.common.TestDescription;
import com.demo.model.Account;
import com.demo.model.AccountRole;
import com.demo.service.AccountService;

public class AuthServerConfigTest extends BaseControllerTest{

	@Autowired
	AccountService accountService;
	
//	최초 발급 : Password Grant
//	Refresh : Refresh Grant
	@Test
	@TestDescription("인증 토큰을 발급 받는 테스트")
	public void getAuthToken() throws Exception {
		// Given
		Set roleSet = new HashSet();
		roleSet.add(AccountRole.ADMIN);
		roleSet.add(AccountRole.USER);
		String password = "1234";
		String email = "test@gmail.com";
		Account account = Account.builder()
				.email(email)
				.password(password)
				.roles(roleSet)
				.build();
		accountService.saveAccount(account);
		String clientId = "client";
		String clientSecret = "secret";
		
		
		mockMvc.perform(post("/oauth/token")
				.with(httpBasic(clientId, clientSecret))
				.param("username", email)
				.param("password", password)
				.param("grant_type", "password"))
		.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("access_token").exists());
		
	}
}
