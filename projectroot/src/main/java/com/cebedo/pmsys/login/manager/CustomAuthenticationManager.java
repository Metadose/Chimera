package com.cebedo.pmsys.login.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cebedo.pmsys.common.AuthHelper;
import com.cebedo.pmsys.common.LogHelper;
import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.security.securityaccess.model.SecurityAccess;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
import com.cebedo.pmsys.systemuser.model.SystemUser;
import com.cebedo.pmsys.systemuser.service.SystemUserService;

/**
 * A custom authentication manager that allows access if the user details exist
 * in the database and if the username and password are not the same. Otherwise,
 * throw a {@link BadCredentialsException}
 */
public class CustomAuthenticationManager implements AuthenticationManager,
		ServletContextAware {

	private static Logger logger = Logger
			.getLogger(SystemConstants.LOGGER_LOGIN);
	private LogHelper logHelper = new LogHelper();
	private SystemUserService systemUserService;
	private ServletContext servletContext;
	private AuthHelper authHelper = new AuthHelper();

	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		SystemUser user = null;
		WebAuthenticationDetails details = (WebAuthenticationDetails) auth
				.getDetails();
		String ipAddress = details.getRemoteAddress();

		try {
			WebApplicationContext applicationContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			this.systemUserService = (SystemUserService) applicationContext
					.getBean("systemUserService");
			user = this.systemUserService.searchDatabase(auth.getName());
		} catch (Exception e) {
			String text = this.logHelper.generateLogMessage(ipAddress, null,
					null, null, null, "User does not exist: " + auth.getName());
			logger.warn(text);
			throw new BadCredentialsException(text);
		}

		// Compare passwords.
		// Make sure to encode the password first before comparing.
		if (this.authHelper.isPasswordValid((String) auth.getCredentials(),
				user) == false) {
			String text = this.logHelper.generateLogMessage(ipAddress,
					user.getCompany(), user, user.getStaff(), null,
					"Invalid password.");
			logger.warn(text);
			throw new BadCredentialsException(text);
		}

		// Here's the main logic of this custom authentication manager.
		// Username and password must not be the same to authenticate.
		if (auth.getName().equals(auth.getCredentials()) == true) {
			String text = this.logHelper.generateLogMessage(ipAddress,
					user.getCompany(), user, user.getStaff(), null,
					"Username and password are the same.");
			logger.warn(text);
			throw new BadCredentialsException(text);

		} else {
			// TODO Check if the user's company is expired.
			AuthenticationToken token = new AuthenticationToken(auth.getName(),
					auth.getCredentials(), getAuthorities(user),
					user.getStaff(), user.getCompany(), user.isSuperAdmin(),
					user.isCompanyAdmin(), user);
			token.setIpAddress(ipAddress);
			logger.info(this.logHelper.generateLogMessage(token,
					"User is authenticated."));
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
		Set<SecurityAccess> accessSet = user.getSecurityAccess();
		Set<SecurityRole> roles = user.getSecurityRoles();
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

		// Add all defined access.
		for (SecurityAccess access : accessSet) {
			authList.add(new SimpleGrantedAuthority(access.getName()));
		}

		// Add all roles.
		for (SecurityRole role : roles) {
			authList.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authList;
	}
}
