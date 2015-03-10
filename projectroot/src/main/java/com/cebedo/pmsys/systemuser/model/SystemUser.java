package com.cebedo.pmsys.systemuser.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.staff.model.Staff;

@Entity
@Table(name = SystemUser.TABLE_NAME)
public class SystemUser {

	public static final String TABLE_NAME = "system_users";
	public static final String OBJECT_NAME = "systemuser";
	public static final String COLUMN_PRIMARY_KEY = "user_id";
	public static final String COLUMN_USER_NAME = "username";
	public static final String COLUMN_PASSWORD = "password";
	public static final String COLUMN_ACCESS = "access";

	private long id;
	private String username;
	private String password;
	private Integer access;
	private Staff staff;
	private boolean superAdmin;
	private Company company;
	private boolean companyAdmin;

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

	@Column(name = "access", nullable = false, length = 3)
	public Integer getAccess() {
		return access;
	}

	public void setAccess(Integer access) {
		this.access = access;
	}

	@OneToOne(cascade = CascadeType.ALL)
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

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
