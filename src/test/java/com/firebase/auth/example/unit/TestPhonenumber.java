package com.firebase.auth.example.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.firebase.auth.example.constant.CommonConstant;
import com.firebase.auth.example.utils.ValidationUtils;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestPhonenumber {

	public TestPhonenumber() {
	}

	@Test
	public void testPhonenumberInfo() {
		PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
		Set<PhoneNumberType> supportTypes = instance.getSupportedTypesForRegion(CommonConstant.VIETNAM_REGION_CODE);
		for (PhoneNumberType phoneNumberType : supportTypes) {
			PhoneNumber exampleNumberForType = instance.getExampleNumberForType(CommonConstant.VIETNAM_REGION_CODE,
					phoneNumberType);
			log.info("PhoneNumberType: {} - Example: {}", phoneNumberType.toString(), exampleNumberForType);
			log.info("Mobile dialing with format: {} - without format:  {}",
					instance.formatNumberForMobileDialing(exampleNumberForType, CommonConstant.VIETNAM_REGION_CODE,
							true),
					instance.formatNumberForMobileDialing(exampleNumberForType, CommonConstant.VIETNAM_REGION_CODE,
							false));
			for (PhoneNumberFormat phoneNumberFormat : PhoneNumberFormat.values()) {
				log.info("{} format: {}", phoneNumberFormat.toString(),
						instance.format(exampleNumberForType, phoneNumberFormat));
			}
		}

	}

	@Test
	public void testPhonenumberValidation() {
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		assertThat(ValidationUtils.getInstance().isValidPhonenumber("0961234567")).isTrue();
		assertThat(ValidationUtils.getInstance().isValidPhonenumber("961234567")).isTrue();
		assertTrue(ValidationUtils.getInstance().isValidPhonenumber("+84987654321"));
		assertTrue(ValidationUtils.getInstance()
				.isValidPhonenumber(String.valueOf(phoneNumberUtil
						.getExampleNumberForType(CommonConstant.VIETNAM_REGION_CODE, PhoneNumberType.MOBILE)
						.getNationalNumber())));

		assertFalse(ValidationUtils.getInstance().isValidPhonenumber(phoneNumberUtil.format(
				phoneNumberUtil.getInvalidExampleNumber(CommonConstant.VIETNAM_REGION_CODE), PhoneNumberFormat.E164)));
		assertFalse(ValidationUtils.getInstance().isValidPhonenumber(""));
		assertFalse(ValidationUtils.getInstance().isValidPhonenumber(null));
		assertFalse(ValidationUtils.getInstance().isValidPhonenumber(
				phoneNumberUtil.format(phoneNumberUtil.getExampleNumberForType(CommonConstant.VIETNAM_REGION_CODE,
						PhoneNumberType.FIXED_LINE_OR_MOBILE), PhoneNumberFormat.E164)));
		assertFalse(ValidationUtils.getInstance().isValidPhonenumber(phoneNumberUtil.format(
				phoneNumberUtil.getExampleNumberForType("CH", PhoneNumberType.MOBILE), PhoneNumberFormat.E164)));
		assertFalse(ValidationUtils.getInstance().isValidPhonenumber(phoneNumberUtil.format(
				phoneNumberUtil.getExampleNumberForType("US", PhoneNumberType.MOBILE), PhoneNumberFormat.E164)));

		Set<PhoneNumberType> supportTypes = PhoneNumberUtil.getInstance()
				.getSupportedTypesForRegion(CommonConstant.VIETNAM_REGION_CODE);
		for (PhoneNumberType phoneNumberType : supportTypes) {
			if (PhoneNumberType.MOBILE == phoneNumberType) {
				continue;
			}
			PhoneNumber exampleNumberForType = PhoneNumberUtil.getInstance()
					.getExampleNumberForType(CommonConstant.VIETNAM_REGION_CODE, phoneNumberType);
			assertFalse(ValidationUtils.getInstance()
					.isValidPhonenumber(phoneNumberUtil.format(exampleNumberForType, PhoneNumberFormat.E164)));
		}

	}

}
