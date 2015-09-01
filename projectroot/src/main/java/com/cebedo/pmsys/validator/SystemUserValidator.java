package com.cebedo.pmsys.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryErrorCodes;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.model.SystemUser;

@Component
public class SystemUserValidator implements Validator {

    private static final String PATTERN_USERNAME = "^[a-z0-9_-]{4,32}$";
    private static final String PATTERN_PASSWORD = "^(?=.*\\d).{8,16}$";

    private final Pattern patternUsername = Pattern.compile(PATTERN_USERNAME);
    private final Pattern patternPassword = Pattern.compile(PATTERN_PASSWORD);
    private Matcher matcher;

    @Override
    public boolean supports(Class<?> clazz) {
	return SystemUser.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

	SystemUser systemUser = (SystemUser) target;

	String username = systemUser.getUsername();
	String password = systemUser.getPassword();
	String passwordRe = systemUser.getRetypePassword();

	// If the username and password are the same.
	if (username.equals(password)) {
	    errors.reject(RegistryErrorCodes.AUTH_USER_PASS_EQUAL,
		    RegistryResponseMessage.ERROR_AUTH_USERNAME_PASSWORD_EQUAL);
	}

	// If the password and re-type password are not the same.
	if (!password.equals(passwordRe)) {
	    errors.reject(RegistryErrorCodes.AUTH_PASSWORDS,
		    RegistryResponseMessage.ERROR_AUTH_PASSWORDS_NOT_EQUAL);
	}

	// Check if the user name is valid.
	this.matcher = this.patternUsername.matcher(username);
	if (!this.matcher.matches()) {
	    errors.reject(RegistryErrorCodes.AUTH_PATTERN_USERNAME,
		    RegistryResponseMessage.ERROR_AUTH_USERNAME_INVALID_PATTERN);
	}

	// Check if the password is valid.
	this.matcher = this.patternPassword.matcher(password);
	if (!this.matcher.matches()) {
	    errors.reject(RegistryErrorCodes.AUTH_PATTERN_PASSWORD,
		    RegistryResponseMessage.ERROR_AUTH_PASSWORD_INVALID_PATTERN);
	}
    }
}
