package com.cebedo.pmsys.security.audit.model;

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

import com.cebedo.pmsys.common.AuthHelper;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.systemuser.model.SystemUser;

@Entity
@Table(name = AuditLog.TABLE_NAME)
public class AuditLog implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String OBJECT_NAME = "auditlog";
	public static final String TABLE_NAME = "audit_logs";
	public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";

	public static final int ACTION_CREATE = 1;
	public static final int ACTION_UPDATE = 2;
	public static final int ACTION_DELETE = 3;

	private AuthHelper authHelper = new AuthHelper();

	public static final String PROPERTY_ID = "id";

	private long id;
	private Date dateExecuted;
	private String ipAddress;
	private SystemUser user;
	private Company company;
	private int action;
	private String objectName;
	private long objectID;

	public AuditLog() {
		setDetails();
	}

	public AuditLog(int action) {
		this.setAction(action);
		setDetails();
	}

	private void setDetails() {
		AuthenticationToken auth = this.authHelper.getAuth();
		if (auth != null) {
			Date dateExecuted = new Date(System.currentTimeMillis());
			this.setIpAddress(auth.getIpAddress());
			this.setDateExecuted(dateExecuted);
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
		this.objectName = objectName;
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
		this.ipAddress = ipAddress;
	}

}
