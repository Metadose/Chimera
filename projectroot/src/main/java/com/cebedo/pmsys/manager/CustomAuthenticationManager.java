package com.cebedo.pmsys.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.BeanHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.service.SystemUserService;
import com.cebedo.pmsys.token.AuthenticationToken;

/**
 * A custom authentication manager that allows access if the user details exist
 * in the database and if the username and password are not the same. Otherwise,
 * throw a {@link BadCredentialsException}
 */
public class CustomAuthenticationManager implements AuthenticationManager, ServletContextAware {

    private static Logger logger = Logger.getLogger(SystemConstants.LOGGER_LOGIN);
    private LogHelper logHelper = new LogHelper();
    private AuthHelper authHelper = new AuthHelper();
    private BeanHelper beanHelper = new BeanHelper();
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
	// TODO Put alert boxes.
	catch (Exception e) {
	    String text = this.logHelper.logMessage(ipAddress, null, null, null, null,
		    "User does not exist: " + auth.getName());
	    logger.warn(text);
	    throw new BadCredentialsException(text);
	}

	if (!user.isSuperAdmin()) {
	    // If the current date is already after the company's expiration.
	    if (new Date(System.currentTimeMillis()).after(user.getCompany().getDateExpiration())) {
		String text = this.logHelper.logMessage(ipAddress, user.getCompany(), user,
			user.getStaff(), null, "User company is expired.");
		logger.warn(text);
		throw new BadCredentialsException(text);
	    }

	    // If user is locked.
	    if (user.getLoginAttempts() > MAX_LOGIN_ATTEMPT) {
		String text = this.logHelper.logMessage(ipAddress, user.getCompany(), user,
			user.getStaff(), null, "User account is locked.");
		logger.warn(text);
		throw new BadCredentialsException(text);
	    }
	}

	// Compare passwords.
	// Make sure to encode the password first before comparing.
	if (this.authHelper.isPasswordValid((String) auth.getCredentials(), user) == false) {
	    // Add 1 to the user login attempts.
	    user.setLoginAttempts(user.getLoginAttempts() + 1);
	    this.systemUserService.update(user, true);

	    String text = this.logHelper.logMessage(ipAddress, user.getCompany(), user, user.getStaff(),
		    null, "Invalid password.");
	    logger.warn(text);
	    throw new BadCredentialsException(text);
	}

	// Here's the main logic of this custom authentication manager.
	// Username and password must not be the same to authenticate.
	if (auth.getName().equals(auth.getCredentials()) == true) {
	    String text = this.logHelper.logMessage(ipAddress, user.getCompany(), user, user.getStaff(),
		    null, "Username and password are the same.");
	    logger.warn(text);
	    throw new BadCredentialsException(text);

	} else {
	    if (user.getLoginAttempts() > 0) {
		user.setLoginAttempts(0);
		this.systemUserService.update(user, true);
	    }

	    AuthenticationToken token = new AuthenticationToken(auth.getName(), auth.getCredentials(),
		    getAuthorities(user), user.getStaff(), user.getCompany(), user.isSuperAdmin(),
		    user.isCompanyAdmin(), user);
	    token.setIpAddress(ipAddress);
	    logger.info(this.logHelper.logMessage(token, "User is authenticated."));
	    return token;
	}
    }

    /**
     * Get all the granted authorities from a specific user.
     * 
     * @param user
     * @return
     */
    public Collection<GrantedAuthority> getAuthorities(SystemUser user) {
	List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
	// authList.add(new SimpleGrantedAuthority(access.getName()));
	return authList;
    }

}
