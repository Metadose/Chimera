package com.cebedo.pmsys.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.cebedo.pmsys.base.IObjectModel;
import com.cebedo.pmsys.utils.NumberFormatUtils;

@Entity
@Table(name = Staff.TABLE_NAME)
public class Staff implements IObjectModel {

    private static final long serialVersionUID = 8510201653144668336L;
    public static final String OBJECT_NAME = "staff";
    public static final String TABLE_NAME = "staff";
    public static final String COLUMN_PRIMARY_KEY = "staff_id";

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_PREFIX = "prefix";
    public static final String PROPERTY_FIRST_NAME = "firstName";
    public static final String PROPERTY_MIDDLE_NAME = "middleName";
    public static final String PROPERTY_LAST_NAME = "lastName";
    public static final String PROPERTY_SUFFIX = "suffix";
    public static final String PROPERTY_WAGE = "wage";

    public static final String PROPERTY_TRANSIENT_FULL_NAME = "Full Name";

    public static final String SUB_MODULE_PROFILE = "profile";

    private long id;
    private String prefix = "";
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String suffix = "";
    private String companyPosition;
    private Set<Task> tasks;

    private String email;
    private String contactNumber;
    private Company company;
    private SystemUser user;
    private double wage;

    public Staff() {
	;
    }

    public Staff(long id) {
	setId(id);
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

    @Column(name = "name_prefix", length = 8)
    public String getPrefix() {
	return prefix;
    }

    public void setPrefix(String prefix) {
	this.prefix = StringUtils.trim(prefix);
    }

    @Column(name = "name_first", length = 32)
    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = StringUtils.trim(firstName);
    }

    @Column(name = "name_middle", length = 16)
    public String getMiddleName() {
	return middleName;
    }

    public void setMiddleName(String middleName) {
	this.middleName = StringUtils.trim(middleName);
    }

    @Column(name = "name_last", length = 16)
    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = StringUtils.trim(lastName);
    }

    @Column(name = "name_suffix", length = 8)
    public String getSuffix() {
	return suffix;
    }

    public void setSuffix(String suffix) {
	this.suffix = StringUtils.trim(suffix);
    }

    @Column(name = "position", length = 32)
    public String getCompanyPosition() {
	return companyPosition;
    }

    public void setCompanyPosition(String companyPosition) {
	this.companyPosition = StringUtils.trim(companyPosition);
    }

    @ManyToMany(mappedBy = "staff")
    public Set<Task> getTasks() {
	return tasks;
    }

    public void setTasks(Set<Task> tasks) {
	this.tasks = tasks;
    }

    @Column(name = "email", length = 32)
    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = StringUtils.trim(email);
    }

    @Column(name = "wage")
    public double getWage() {
	return wage;
    }

    @Transient
    public String getWageAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(wage);
    }

    public void setWage(double wage) {
	this.wage = wage;
    }

    @Column(name = "contact_number", length = 32)
    public String getContactNumber() {
	return contactNumber;
    }

    @Transient
    public String getContactNumberAsString() {
	return getContactNumber() == null ? "" : getContactNumber();
    }

    public void setContactNumber(String contactNumber) {
	this.contactNumber = StringUtils.trim(contactNumber);
    }

    @ManyToOne
    @JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @OneToOne(mappedBy = Staff.OBJECT_NAME, cascade = CascadeType.REMOVE)
    public SystemUser getUser() {
	return user;
    }

    public void setUser(SystemUser user) {
	this.user = user;
    }

    @Transient
    public boolean isNameSet() {
	return !(StringUtils.isBlank(getPrefix()) && StringUtils.isBlank(getFirstName())
		&& StringUtils.isBlank(getMiddleName()) && StringUtils.isBlank(getLastName())
		&& StringUtils.isBlank(getSuffix()));
    }

    @Transient
    public String getFullName() {
	String middleInitial = StringUtils.capitalize(
		(String) (getMiddleName().length() > 0 ? getMiddleName().charAt(0) + "." : ""));
	if (isNameSet()) {
	    String name = getPrefix() + " " + getFirstName() + " " + middleInitial + " " + getLastName()
		    + " " + getSuffix();
	    return StringUtils.trim(name.replaceAll("  ", " "));
	}
	return "(No Name)";
    }

    @Transient
    public String getFullNameWithMiddleName() {
	if (isNameSet()) {
	    String name = getPrefix() + " " + getFirstName() + " " + getMiddleName() + " "
		    + getLastName() + " " + getSuffix();
	    return StringUtils.trim(name.replaceAll("  ", " "));
	}
	return "(No Name)";
    }

    @Transient
    public String getFormalName() {
	if (!isNameSet()) {
	    return "(No Name)";
	}
	String pfx = getPrefix() == null || getPrefix().equals("") ? "" : ", " + getPrefix();
	String fname = getFirstName() == null ? "" : getFirstName();
	String mname = getMiddleName() == null || getMiddleName().equals("") ? ""
		: ", " + getMiddleName().charAt(0) + ".";
	String lname = getLastName() == null ? "" : getLastName();
	String sfx = getSuffix() == null ? "" : getSuffix();
	String name = lname + ", " + fname + " " + sfx + mname + pfx;
	return StringUtils.trim(name);
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof Staff ? ((Staff) obj).getId() == (getId()) : false;
    }

    @Override
    public int hashCode() {
	return ((Long) getId()).hashCode();
    }

    @Override
    public String toString() {
	return getId() + ": " + getFullNameWithMiddleName();
    }

    @Transient
    @Override
    public String getName() {
	return getFullName();
    }

    @Transient
    @Override
    public String getObjectName() {
	return OBJECT_NAME;
    }

    @Transient
    @Override
    public String getTableName() {
	return TABLE_NAME;
    }

    public Staff clone() {
	try {
	    return (Staff) super.clone();
	} catch (Exception e) {
	    return null;
	}
    }

}
