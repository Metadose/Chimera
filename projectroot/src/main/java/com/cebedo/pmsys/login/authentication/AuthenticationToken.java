package com.cebedo.pmsys.login.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.staff.model.Staff;

public class AuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	// ~ Instance fields
	// ================================================================================================

	private final Object principal;
	private Object credentials;
	private Staff staff;
	private Company company;
	private boolean superAdmin;
	private boolean companyAdmin;

	// ~ Constructors
	// ===================================================================================================

	/**
	 * This constructor can be safely used by any code that wishes to create a
	 * <code>AuthenticationToken</code>, as the {@link #isAuthenticated()} will
	 * return <code>false</code>.
	 *
	 */
	public AuthenticationToken(Object principal, Object credentials) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(false);
	}

	/**
	 * This constructor should only be used by
	 * <code>AuthenticationManager</code> or <code>AuthenticationProvider</code>
	 * implementations that are satisfied with producing a trusted (i.e.
	 * {@link #isAuthenticated()} = <code>true</code>) authentication token.
	 *
	 * @param principal
	 * @param credentials
	 * @param authorities
	 */
	public AuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true); // must use super, as we override
	}

	public AuthenticationToken(String name, Object cred,
			Collection<GrantedAuthority> authorities, Staff staffMember,
			Company com, boolean superAdministrator,
			boolean companyAdministrator) {
		super(authorities);
		this.principal = name;
		this.credentials = cred;
		super.setAuthenticated(true); // must use super, as we override

		setStaff(staffMember);
		setCompany(com);
		setSuperAdmin(superAdministrator);
		setCompanyAdmin(companyAdministrator);
	}

	// ~ Methods
	// ========================================================================================================

	public Object getCredentials() {
		return this.credentials;
	}

	public Object getPrincipal() {
		return this.principal;
	}

	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Once created you cannot set this token to authenticated. Create a new instance using the constructor which takes a GrantedAuthority list will mark this as authenticated.");
		}

		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		credentials = null;
	}

	public static AuthenticationToken get() {
		return (AuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.superAdmin = isSuperAdmin;
	}

	public boolean isCompanyAdmin() {
		return companyAdmin;
	}

	public void setCompanyAdmin(boolean companyAdmin) {
		this.companyAdmin = companyAdmin;
	}
}
