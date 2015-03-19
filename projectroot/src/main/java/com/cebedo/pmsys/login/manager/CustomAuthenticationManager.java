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
					auth.getCredentials(), getAuthorities(user),
					user.getStaff(), user.getCompany(), user.isSuperAdmin(),
					user.isCompanyAdmin(), user);
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
