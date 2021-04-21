package com.firebase.auth.example.constant;

import java.util.regex.Pattern;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class CommonConstant {

	public static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(new AntPathRequestMatcher("/public/**"));
	public static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

	public static final String EMAIL_SIGN = "@";
	public static final String EMAIL_REGEX = "^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$";
	public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	public static final String VIETNAM_REGION_CODE = "VN";

	public static final String DEFAULT_RSA_ALGORITHM = "RSA";
	public static final String DEFAULT_REFRESH_TOKEN_KEY = "__rftk";
	public static final String JWT_AUTHORZIED_KEY = "roles";
	public static final String JWT_SUBJECT_KEY = "sub";
	public static final String JWT_ACCOUNT_ID_KEY = "accId";

}
