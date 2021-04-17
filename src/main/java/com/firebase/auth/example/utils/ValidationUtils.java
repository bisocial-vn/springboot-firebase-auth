package com.firebase.auth.example.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.firebase.auth.example.constant.CommonConstant;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class ValidationUtils {

	private static volatile ValidationUtils INSTANCE = null;

	private ValidationUtils() {
		if (INSTANCE != null) {
			throw new RuntimeException("Already initial.");
		}
	}

	public static ValidationUtils getInstance() {
		if (INSTANCE == null) {
			synchronized (ValidationUtils.class) {
				if (INSTANCE == null) {
					INSTANCE = new ValidationUtils();
				}
			}
		}
		return INSTANCE;
	}

	public boolean isValidPhonenumber(String phonenumber) {
		if (!StringUtils.hasText(phonenumber)) {
			return false;
		}
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		try {
			PhoneNumber phonenumberParser = phoneNumberUtil.parseAndKeepRawInput(phonenumber,
					CommonConstant.VIETNAM_REGION_CODE);
			boolean isPossiableMobilePhoneNumber = phoneNumberUtil.isPossibleNumberForType(phonenumberParser,
					PhoneNumberType.MOBILE);
			if (!isPossiableMobilePhoneNumber) {
				return false;
			}
			boolean isValidPhonenumber = phoneNumberUtil.isValidNumberForRegion(phonenumberParser,
					CommonConstant.VIETNAM_REGION_CODE);
			if (!isValidPhonenumber) {
				return false;
			}
			PhoneNumberType phoneNumberType = phoneNumberUtil.getNumberType(phonenumberParser);
			return PhoneNumberType.MOBILE == phoneNumberType;
		} catch (NumberParseException ex) {
			return false;
		}
	}

	public boolean isValidEmail(String email) {
		return this.isMatchPattern(CommonConstant.EMAIL_PATTERN, email);
	}

	public void validateLoginName(String loginname) throws RuntimeException {
		if (!StringUtils.hasText(loginname)) {
			throw new IllegalArgumentException("Login name is required.");
		}
		if (loginname.contains(CommonConstant.EMAIL_SIGN)) {
			if (!this.isValidEmail(loginname)) {
				throw new IllegalArgumentException("Invalid email format.");
			}
			return;
		}
		if (!this.isValidPhonenumber(loginname)) {
			throw new IllegalArgumentException("Invalid phonenumber.");
		}
	}

	public void validatePassword(String password) throws RuntimeException {
		if (!StringUtils.hasText(password)) {
			throw new IllegalArgumentException("Password is required.");
		}
		if (password.length() < 6) {
			throw new IllegalArgumentException("Password atlest 6 char.");
		}
	}

	public boolean isMatchPattern(String regex, String input) {
		if (!StringUtils.hasText(input) || !StringUtils.hasText(regex)) {
			return false;
		}
		Pattern tmpPattern = Pattern.compile(regex);
		Matcher matcher = tmpPattern.matcher(input);
		return matcher.find();
	}

	public boolean isMatchPattern(Pattern pattern, String input) {
		if (!StringUtils.hasText(input) || pattern == null || !StringUtils.hasText(pattern.pattern())) {
			return false;
		}
		Matcher matcher = pattern.matcher(input);
		return matcher.find();
	}

}
