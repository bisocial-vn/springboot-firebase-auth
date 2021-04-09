package com.firebase.auth.example.configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.firebase.auth.example.constant.CommonConstant;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
		http.csrf().disable();
		http.formLogin().disable();
		http.logout().disable();
		http.cors(Customizer.withDefaults());
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		http.exceptionHandling().defaultAuthenticationEntryPointFor(, CommonConstant.PROTECTED_URLS)
		http.authorizeRequests().requestMatchers(CommonConstant.PROTECTED_URLS).authenticated();
		http.authorizeRequests().antMatchers("/h2-ui/**").permitAll();
		http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(CommonConstant.PUBLIC_URLS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		String defaultEncoder = "pbkdf2";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
//		encoders.put("noop", NoOpPasswordEncoder.getInstance());
//		encoders.put("sha256", new StandardPasswordEncoder());

		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(defaultEncoder, encoders);
		return passwordEncoder;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(false);
		configuration.setMaxAge(Duration.ofDays(1));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		// TODO add cors config for auth / PROTECTED_URLS
		return source;
	}

}
