package com.bi.firebase.auth.example.endpoint;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class FallbackErrorHandler implements ErrorController {

	private static final String DEFAULT_ERROR_PATH = "/public/error";
	private ErrorAttributes errorAttributes;

	@Value("${server.error.path:/public/error}")
	private String errorPath;
	@Value("${debug:false}")
	private boolean isDebug = false;

	public FallbackErrorHandler(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	@Override
	public String getErrorPath() {
		return errorPath;
	}

	@RequestMapping(DEFAULT_ERROR_PATH)
	public ResponseEntity<?> errorHandler(WebRequest request, HttpServletResponse response) {
		ErrorAttributeOptions attributeOptions = null;
		if (isDebug) {
			attributeOptions = ErrorAttributeOptions.of(Include.values());
		} else {
			attributeOptions = ErrorAttributeOptions.defaults();
		}
		Map<String, Object> errorBodyMap = this.errorAttributes.getErrorAttributes(request, attributeOptions);
		return ResponseEntity.status(response.getStatus()).body(errorBodyMap);
	}

}
