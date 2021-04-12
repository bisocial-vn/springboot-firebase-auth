package com.firebase.auth.example.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.firebase.auth.example.constant.CommonConstant;
import com.firebase.auth.example.repository.AccountRepository;
import com.firebase.auth.example.utils.ValidationUtils;

@Service
public class AccountDetailsService implements UserDetailsService {

	private AccountRepository accountRepository;

	public AccountDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (!StringUtils.hasText(username)) {
			throw new UsernameNotFoundException("Login name empty.");
		}
		ValidationUtils.getInstance().validateLoginName(username);
		if (username.contains(CommonConstant.EMAIL_SIGN)) {
			return this.accountRepository.findByEmail(username).map(AccountDetails::new)
					.orElseThrow(() -> new UsernameNotFoundException("Not found."));
		}
		return this.accountRepository.findByPhone(username).map(AccountDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("Not found."));
	}

}
