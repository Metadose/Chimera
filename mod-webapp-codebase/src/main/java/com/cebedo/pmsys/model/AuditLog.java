package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.utils.DateUtils;

@Entity
@Table(name = AuditLog.TABLE_NAME)
public class AuditLog implements Serializable {

    private static final long serialVersionUID = -3443728849263419668L;
    public static final String OBJECT_NAME = "auditlog";
    public static final String TABLE_NAME = "audit_logs";
    public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";

    private AuthHelper authHelper = new AuthHelper();

    public static final String PROPERTY_ID = "id";

    private long id;
    private Company company;
    private String objectName;
    private long objectID;
    private int action;

    // To display:
    private Date dateExecuted;
    private String ipAddress;
    private SystemUser user;
    private AuditAction auditAction;

    public AuditLog() {
	setDetails();
    }

    public AuditLog(int action) {
	setAction(action);
	setDetails();
    }

    public AuditLog(int action, AuthenticationToken auth) {
	setAction(action);
	setDateExecuted(new Date(System.currentTimeMillis()));
	if (auth != null) {
	    setIpAddress(auth.getIpAddress());
	    setUser(auth.getUser());
	}
    }

    public AuditLog(int action, SystemUser usr, String ipAddr, Company company2, String objName,
	    long objID) {
	setAction(action);
	setUser(usr);
	setDateExecuted(new Date(System.currentTimeMillis()));
	setIpAddress(ipAddr);
	setCompany(company2);
	setObjectName(objName);
	setObjectID(objID);
    }

    private void setDetails() {
	AuthenticationToken auth = this.authHelper.getAuth();
	Date dateExecuted = new Date(System.currentTimeMillis());
	this.setDateExecuted(dateExecuted);
	if (auth != null) {
	    this.setIpAddress(auth.getIpAddress());
	    this.setUser(auth.getUser());
	}
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

    @Column(name = "date_executed", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateExecuted() {
	return dateExecuted;
    }

    @Transient
    public String getDateExecutedAsString() {
	return DateUtils.formatDate(getDateExecuted(), DateUtils.PATTERN_DATE_TIME);
    }

    public void setDateExecuted(Date dateExecuted) {
	this.dateExecuted = dateExecuted;
    }

    @ManyToOne
    @JoinColumn(name = SystemUser.COLUMN_PRIMARY_KEY)
    public SystemUser getUser() {
	return user;
    }

    public void setUser(SystemUser user) {
	this.user = user;
    }

    @Column(name = "action", length = 4, nullable = false)
    public int getAction() {
	return action;
    }

    public void setAction(int action) {
	this.action = action;
    }

    @Column(name = "object_name", length = 64, nullable = false)
    public String getObjectName() {
	return objectName;
    }

    public void setObjectName(String objectName) {
	this.objectName = StringUtils.trim(objectName);
    }

    @Column(name = "object_id", nullable = false)
    public long getObjectID() {
	return objectID;
    }

    public void setObjectID(long objectID) {
	this.objectID = objectID;
    }

    @ManyToOne
    @JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @Column(name = "ip_address", nullable = false, length = 15)
    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = StringUtils.trim(ipAddress);
    }

    @Transient
    public AuditAction getAuditAction() {
	return auditAction;
    }

    public void setAuditAction(AuditAction auditAction) {
	this.auditAction = auditAction;
    }

}
