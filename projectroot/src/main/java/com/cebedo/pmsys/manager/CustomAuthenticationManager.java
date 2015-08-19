package com.cebedo.pmsys.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
public class CustomAuthenticationManager implements AuthenticationManager, ServletContextAware {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private static final int MAX_LOGIN_ATTEMPT = 5;

    private SystemUserService systemUserService;
    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext context) {
	this.servletContext = context;
    }

    public Authentication authenticate(Authentication auth) throws AuthenticationException {
	SystemUser user = null;
	WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
	String ipAddress = details.getRemoteAddress();

	// Check if the user exists.
	try {
	    WebApplicationContext applicationContext = WebApplicationContextUtils
		    .getWebApplicationContext(servletContext);
	    this.systemUserService = (SystemUserService) applicationContext.getBean("systemUserService");
	    user = this.systemUserService.searchDatabase(auth.getName());
	}
	// If user does not exist.
	catch (Exception e) {
	    // TODO Below line fails.
	    this.messageHelper.loginError(ipAddress, user, AuditAction.LOGIN_USER_NOT_EXIST);
	    throw new BadCredentialsException(AuditAction.LOGIN_USER_NOT_EXIST.label());
	}

	Company company = user.getCompany();
	Staff staff = user.getStaff();
	Object credentials = auth.getCredentials();

	if (!user.isSuperAdmin()) {

	    // If the current date is already after the company's expiration.
	    if (new Date(System.currentTimeMillis()).after(company.getDateExpiration())) {
		this.messageHelper.loginError(ipAddress, user, AuditAction.LOGIN_COMPANY_EXPIRED);
		throw new BadCredentialsException(AuditAction.LOGIN_COMPANY_EXPIRED.label());
	    }

	    // If user is locked.
	    if (user.getLoginAttempts() > MAX_LOGIN_ATTEMPT) {
		this.messageHelper.loginError(ipAddress, user, AuditAction.LOGIN_USER_LOCKED);
		throw new BadCredentialsException(AuditAction.LOGIN_USER_LOCKED.label());
	    }
	}

	// Compare passwords.
	// Make sure to encode the password first before comparing.
	// Add 1 to the user login attempts.
	if (this.authHelper.isPasswordValid((String) credentials, user) == false) {
	    user.setLoginAttempts(user.getLoginAttempts() + 1);
	    this.systemUserService.update(user, true);
	    this.messageHelper.loginError(ipAddress, user, AuditAction.LOGIN_INVALID_PASSWORD);
	    throw new BadCredentialsException(AuditAction.LOGIN_INVALID_PASSWORD.label());
	}

	// Here's the main logic of this custom authentication manager.
	if (user.getLoginAttempts() > 0) {
	    user.setLoginAttempts(0);
	    this.systemUserService.update(user, true);
	}

	AuthenticationToken token = new AuthenticationToken(auth.getName(), credentials,
		getAuthorities(user), staff, company, user.isSuperAdmin(), user.isCompanyAdmin(), user);
	token.setIpAddress(ipAddress);
	this.messageHelper.login(AuditAction.LOGIN_AUTHENTICATED, token);
	return token;
    }

    /**
     * TODO Get all the granted authorities from a specific user.
     * 
     * @param user
     * @return
     */
    @Deprecated
    public Collection<GrantedAuthority> getAuthorities(SystemUser user) {
	List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
	// authList.add(new SimpleGrantedAuthority(access.getName()));
	return authList;
    }

}
