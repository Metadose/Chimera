package com.cebedo.pmsys.login.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.security.securitygroup.model.SecurityGroup;
import com.cebedo.pmsys.systemuser.model.SystemUser;
import com.cebedo.pmsys.systemuser.service.SystemUserService;

/**
 * A custom authentication manager that allows access if the user details exist
 * in the database and if the username and password are not the same. Otherwise,
 * throw a {@link BadCredentialsException}
 */
public class CustomAuthenticationManager implements AuthenticationManager,
		ServletContextAware {

	protected static Logger logger = Logger.getLogger("service");
	private SystemUserService systemUserService;
	private ServletContext servletContext;
	private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {

		logger.debug("Performing custom authentication");

		// Init a database user object
		SystemUser user = null;

		try {
			WebApplicationContext applicationContext = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			this.systemUserService = (SystemUserService) applicationContext
					.getBean("systemUserService");
			user = this.systemUserService.searchDatabase(auth.getName());
		} catch (Exception e) {
			logger.error("User does not exists!");
			throw new BadCredentialsException("User does not exists!");
		}

		// Compare passwords
		// Make sure to encode the password first before comparing
		if (passwordEncoder.isPasswordValid(user.getPassword(),
				(String) auth.getCredentials(), user.getUsername()) == false) {
			logger.error("Wrong password!");
			throw new BadCredentialsException("Wrong password!");
		}

		// Here's the main logic of this custom authentication manager
		// Username and password must be the same to authenticate
		if (auth.getName().equals(auth.getCredentials()) == true) {
			logger.debug("Entered username and password are the same!");
			throw new BadCredentialsException(
					"Entered username and password are the same!");

		} else {
			// TODO Check if the user's company is expired.
			logger.debug("User dtails are good and ready to go");
			return new AuthenticationToken(auth.getName(),
					auth.getCredentials(),
					getAuthorities(user.getSecurityGroups()), user.getStaff(),
					user.getCompany(), user.isSuperAdmin(),
					user.isCompanyAdmin(), user);
		}
	}

	public Collection<GrantedAuthority> getAuthorities(Set<SecurityGroup> groups) {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList.add(new SimpleGrantedAuthority("ROLE_AUTH_USER"));
		for (SecurityGroup group : groups) {
			authList.add(new SimpleGrantedAuthority(group.getName()));
		}
		return authList;
	}

	/**
	 * Retrieves the correct ROLE type depending on the access level, where
	 * access level is an Integer. Basically, this interprets the access value
	 * whether it's for a regular user or admin.
	 * 
	 * @param access
	 *            an integer value representing the access of the user
	 * @return collection of granted authorities
	 */
	// public Collection<GrantedAuthority> getAuthorities(Integer access) {
	// List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
	//
	// // All users are granted with ROLE_USER access.
	// // Therefore this user gets a ROLE_USER by default.
	// authList.add(new SimpleGrantedAuthority("ROLE_USER"));
	//
	// // Check if this user has super admin access.
	// if (access.compareTo(1) == 0) {
	// authList.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
	// authList.add(new SimpleGrantedAuthority("ROLE_COMPANY_ADMIN"));
	// }
	//
	// // Check if this user has company admin access.
	// if (access.compareTo(2) == 0) {
	// authList.add(new SimpleGrantedAuthority("ROLE_COMPANY_ADMIN"));
	// }
	// return authList;
	// }

}
