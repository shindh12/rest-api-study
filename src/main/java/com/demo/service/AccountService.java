package com.demo.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.model.Account;
import com.demo.model.AccountRole;
import com.demo.repo.AccountRepository;

@Service
public class AccountService implements UserDetailsService{

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	public Account saveAccount(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		return accountRepository.save(account);
	}
	public List<Account> findAllAccount() {
		return accountRepository.findAll();
	}
	
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(name)
				.orElseThrow(() -> new UsernameNotFoundException(name));
		return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
	}

	private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
//		List<GrantedAuthority> auths = new ArrayList<>();
//		for (AccountRole r : roles) {
//			auths.add(new SimpleGrantedAuthority("ROLE " + r.name()));
//		}
//		return auths;
		return roles.stream()
				.map(r -> new SimpleGrantedAuthority("ROLE " + r.name()))
				.collect(Collectors.toSet());
	}
	
}
