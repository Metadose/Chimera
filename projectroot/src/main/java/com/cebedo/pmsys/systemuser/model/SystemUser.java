package com.cebedo.pmsys.systemuser.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.security.audit.model.AuditLog;
import com.cebedo.pmsys.security.securityaccess.model.SecurityAccess;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
import com.cebedo.pmsys.staff.model.Staff;

@Entity
@Table(name = SystemUser.TABLE_NAME)
public class SystemUser implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "system_users";
	public static final String OBJECT_NAME = "systemuser";
	public static final String COLUMN_PRIMARY_KEY = "user_id";
	public static final String COLUMN_USER_NAME = "username";
	public static final String COLUMN_PASSWORD = "password";
	public static final String COLUMN_ACCESS = "access";

	private long id;
	private String username;
	private String password;
	private Staff staff;
	private boolean superAdmin;
	private Company company;
	private boolean companyAdmin;
	private Set<SecurityAccess> securityAccess;
	private Set<SecurityRole> securityRoles;
	private Set<AuditLog> auditLogs;
	private int loginAttempts;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "username", nullable = false, length = 32)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToOne
	@JoinColumn(name = Staff.COLUMN_PRIMARY_KEY)
	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	@Column(name = "super_admin")
	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	@Column(name = "company_admin")
	public boolean isCompanyAdmin() {
		return companyAdmin;
	}

	public void setCompanyAdmin(boolean companyAdmin) {
		this.companyAdmin = companyAdmin;
	}

	@ManyToOne
	@JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToMany
	@JoinTable(name = UserAccessAssignment.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = SecurityAccess.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
	public Set<SecurityAccess> getSecurityAccess() {
		return securityAccess;
	}

	public void setSecurityAccess(Set<SecurityAccess> securityAccess) {
		this.securityAccess = securityAccess;
	}

	@ManyToMany
	@JoinTable(name = UserRoleAssignment.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = SecurityRole.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
	public Set<SecurityRole> getSecurityRoles() {
		return securityRoles;
	}

	public void setSecurityRoles(Set<SecurityRole> securityRoles) {
		this.securityRoles = securityRoles;
	}

	@OneToMany(mappedBy = "user")
	public Set<AuditLog> getAuditLogs() {
		return auditLogs;
	}

	public void setAuditLogs(Set<AuditLog> auditLogs) {
		this.auditLogs = auditLogs;
	}

	@Column(name = "login_attempts", nullable = false)
	public int getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(int loginAttempts) {
		this.loginAttempts = loginAttempts;
	}
}
