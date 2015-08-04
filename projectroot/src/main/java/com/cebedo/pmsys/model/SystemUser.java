package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = SystemUser.TABLE_NAME)
public class SystemUser implements Serializable {

    private static final long serialVersionUID = -5847055437189444297L;
    public static final String TABLE_NAME = "system_users";
    public static final String OBJECT_NAME = "systemuser";
    public static final String COLUMN_PRIMARY_KEY = "user_id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ACCESS = "access";

    private long id;
    private String username;
    private String password;
    private String retypePassword;
    private Staff staff;
    private boolean superAdmin;
    private Company company;
    private Long companyID;
    private boolean companyAdmin;
    private Set<AuditLog> auditLogs;
    private int loginAttempts;

    public SystemUser() {
	;
    }

    public SystemUser(long userID) {
	this.id = userID;
    }

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

    @Transient
    public String getRetypePassword() {
	return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
	this.retypePassword = retypePassword;
    }

    @Transient
    public Long getCompanyID() {
	return companyID;
    }

    public void setCompanyID(Long companyID) {
	this.companyID = companyID;
    }

    public String toString() {
	return String.valueOf(this.id);
    }
}
