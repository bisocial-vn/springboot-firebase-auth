package com.bi.firebase.auth.example.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import com.bi.firebase.auth.example.configuration.properties.JwtProperties;
import com.bi.firebase.auth.example.constant.CommonConstant;
import com.bi.firebase.auth.example.security.AccountDetailsService;
import com.bi.firebase.auth.example.security.FobiddenHiddenHandler;
import com.bi.firebase.auth.example.security.JwtTokenAuthenticationManager;
import com.bi.firebase.auth.example.security.JwtTokenAuthenticationProcessingFilter;
import com.bi.firebase.auth.example.security.NoRedirectStrategy;
import com.bi.firebase.auth.example.security.UnauthenticationHandler;
import com.bi.firebase.auth.example.service.TokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private AccountDetailsService accountDetailsService;
	private UnauthenticationHandler unauthenticationHandler;
	private FobiddenHiddenHandler fobiddenHiddenHandler;
	private JwtProperties jwtProperties;
	private TokenService tokenService;

	public SecurityConfiguration(AccountDetailsService accountDetailsService,
			UnauthenticationHandler unauthenticationHandler, FobiddenHiddenHandler fobiddenHiddenHandler,
			JwtProperties jwtProperties, TokenService tokenService) {
		super(true);
		this.accountDetailsService = accountDetailsService;
		this.unauthenticationHandler = unauthenticationHandler;
		this.fobiddenHiddenHandler = fobiddenHiddenHandler;
		this.jwtProperties = jwtProperties;
		this.tokenService = tokenService;
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
		http.csrf().disable();
		http.formLogin().disable();
		http.logout().disable();
		http.cors();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.exceptionHandling().defaultAuthenticationEntryPointFor(unauthenticationHandler,
				CommonConstant.PROTECTED_URLS);
		http.exceptionHandling().defaultAccessDeniedHandlerFor(fobiddenHiddenHandler, CommonConstant.PROTECTED_URLS);
		http.authorizeRequests().requestMatchers(CommonConstant.PUBLIC_URLS).permitAll();
		http.authorizeRequests().requestMatchers(CommonConstant.PROTECTED_URLS).authenticated();
		http.authorizeRequests().antMatchers("/h2-ui/**").permitAll();
		http.authorizeRequests().antMatchers("/actuator/**").permitAll();
		http.addFilterBefore(jwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(CommonConstant.PUBLIC_URLS);
	}

	@Bean
	public DaoAuthenticationProvider configDaoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(accountDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(configDaoAuthenticationProvider());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		String defaultEncoder = "pbkdf2";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());

		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(defaultEncoder, encoders);
		return passwordEncoder;
	}

	@Bean
	GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
	}

	@Bean
	SimpleUrlAuthenticationSuccessHandler successHandler() {
		final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
		successHandler.setRedirectStrategy(new NoRedirectStrategy());
		return successHandler;
	}

	@Bean
	JwtTokenAuthenticationProcessingFilter jwtTokenAuthenticationProcessingFilter() throws Exception {
		final JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(
				CommonConstant.PROTECTED_URLS, jwtProperties);
//		filter.setAuthenticationManager(this.authenticationManager());
		filter.setAuthenticationManager(new JwtTokenAuthenticationManager(tokenService));
		filter.setAuthenticationSuccessHandler(successHandler());
		return filter;
	}

	/**
	 * Disable Spring boot automatic filter registration.
	 */
	@Bean
	FilterRegistrationBean<GenericFilterBean> disableAutoRegistration(
			final JwtTokenAuthenticationProcessingFilter tokenFilter) {
		final FilterRegistrationBean<GenericFilterBean> registration = new FilterRegistrationBean<>(tokenFilter);
		registration.setEnabled(false);
		return registration;
	}

}
