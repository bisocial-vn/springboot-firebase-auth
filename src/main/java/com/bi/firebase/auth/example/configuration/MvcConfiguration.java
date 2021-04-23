package com.bi.firebase.auth.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bi.firebase.auth.example.configuration.properties.ApiConfigProperties;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

	@Autowired
	private ApiConfigProperties apiConfigProperties;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/public/auth/token")//
				.allowedOrigins(apiConfigProperties.getAllowCorsDomains().toArray(new String[0])).allowedHeaders("*")
				.allowedMethods("*").allowCredentials(true);
		registry.addMapping("/public/auth/refresh")//
				.allowedOrigins(apiConfigProperties.getAllowCorsDomains().toArray(new String[0])).allowedHeaders("*")
				.allowedMethods("*").allowCredentials(true);
		registry.addMapping("/**")//
				.allowedOrigins(apiConfigProperties.getAllowCorsDomains().toArray(new String[0])).allowedHeaders("*")
				.allowedMethods("*").allowCredentials(false);
	}

}
