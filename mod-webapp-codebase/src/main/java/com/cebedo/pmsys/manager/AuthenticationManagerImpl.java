package com.cebedo.pmsys.manager;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.service.SystemUserService;
import com.cebedo.pmsys.token.AuthenticationToken;

/**
 * A custom authentication manager that allows access if the user details exist
 * in the database and if the username and password are not the same. Otherwise,
 * throw a {@link BadCredentialsException}
 */
public class AuthenticationManagerImpl implements AuthenticationManager {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private static final int MAX_LOGIN_ATTEMPT = 10;

    private SystemUserService systemUserService;

    @Autowired
    public void setSystemUserService(SystemUserService systemUserService) {
	this.systemUserService = systemUserService;
    }

    public Authentication authenticate(Authentication auth) throws AuthenticationException {
	SystemUser user = null;
	WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
	String ipAddress = details.getRemoteAddress();

	// Check if the user exists.
	try {
	    user = this.systemUserService.searchDatabase(auth.getName());
	}
	// If user does not exist.
	catch (Exception e) {
	    this.messageHelper.loginFailed(ipAddress, user, AuditAction.LOGIN_USER_NOT_EXIST);
	    throw new BadCredentialsException(AuditAction.LOGIN_USER_NOT_EXIST.label());
	}

	Company company = user.getCompany();

	Staff staff = user.getStaff();
	Object credentials = auth.getCredentials();

	// If not a super admin.
	if (!user.isSuperAdmin()) {

	    // TODO This feature will be enabled soon when we have a better
	    // cloud machine.
	    // If server is beta, company must also be beta.
	    // boolean isBeta = this.systemConfigurationService.getBetaServer();
	    // if (!(isBeta && company.isBetaTester())) {
	    // this.messageHelper.loginFailed(ipAddress, user,
	    // AuditAction.LOGIN_COMPANY_NOT_BETA);
	    // throw new
	    // BadCredentialsException(AuditAction.LOGIN_COMPANY_NOT_BETA.label());
	    // }

	    // If the current date is already after the company's expiration.
	    Date currentDatetime = new Date(System.currentTimeMillis());
	    Date companyExpire = company.getDateExpiration();
	    if (currentDatetime.after(companyExpire)) {
		this.messageHelper.loginFailed(ipAddress, user, AuditAction.LOGIN_COMPANY_EXPIRED);
		throw new BadCredentialsException(AuditAction.LOGIN_COMPANY_EXPIRED.label());
	    }

	    // If user is locked.
	    if (user.getLoginAttempts() > MAX_LOGIN_ATTEMPT) {
		this.messageHelper.loginFailed(ipAddress, user, AuditAction.LOGIN_USER_LOCKED);
		throw new BadCredentialsException(AuditAction.LOGIN_USER_LOCKED.label());
	    }
	}

	// If password is invalid.
	if (!this.authHelper.isPasswordValid((String) credentials, user)) {
	    // Make sure to encode the password first before comparing.
	    // Add 1 to the user login attempts.
	    user.setLoginAttempts(user.getLoginAttempts() + 1);
	    this.systemUserService.update(user, true);
	    this.messageHelper.loginFailed(ipAddress, user, AuditAction.LOGIN_INVALID_PASSWORD);
	    throw new BadCredentialsException(AuditAction.LOGIN_INVALID_PASSWORD.label());
	}

	// Clear the login attempts.
	if (user.getLoginAttempts() > 0) {
	    user.setLoginAttempts(0);
	    this.systemUserService.update(user, true);
	}

	// Proceed to login.
	AuthenticationToken token = new AuthenticationToken(auth.getName(), credentials,
		getAuthorities(user), staff, company, user.isSuperAdmin(), user.isCompanyAdmin(), user);
	token.setIpAddress(ipAddress);
	this.messageHelper.loginSuccess(token);
	return token;
    }

    /**
     * Get all the granted authorities from a specific user.
     * 
     * @param user
     * @return
     */
    public Collection<GrantedAuthority> getAuthorities(SystemUser user) {
	return this.systemUserService.getAuthorities(user);
    }

}
