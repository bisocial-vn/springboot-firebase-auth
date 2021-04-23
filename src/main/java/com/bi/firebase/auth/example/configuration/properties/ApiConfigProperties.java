package com.bi.firebase.auth.example.configuration.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("app.api")
public class ApiConfigProperties {
	private List<String> allowCorsDomains = new ArrayList<String>();
}
