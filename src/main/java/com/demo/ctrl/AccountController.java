package com.demo.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.Account;
import com.demo.service.AccountService;

@RestController
@RequestMapping("/api/account")
public class AccountController {
	@Autowired
	AccountService accountService;
	
	@GetMapping
	public ResponseEntity<?> findAccount() {
		List<Account> accounts = accountService.findAllAccount();
		return ResponseEntity.ok(accounts);
	}
}
