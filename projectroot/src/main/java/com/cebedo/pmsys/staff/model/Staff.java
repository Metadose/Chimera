package com.cebedo.pmsys.staff.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = Staff.TABLE_NAME)
public class Staff implements Serializable {

	public static final String TABLE_NAME = "staff";
	public static final String COLUMN_PRIMARY_KEY = "staff_id";

	private static final long serialVersionUID = 1L;

	private long id;
	private String thumbnailURL;
	private String prefix;
	private String firstName;
	private String middleName;
	private String lastName;
	private String suffix;
	private String companyPosition;
	private Set<ManagerAssignment> assignedManagers;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "thumbnail_url", length = 255)
	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	@Column(name = "name_prefix", length = 8)
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Column(name = "name_first", nullable = false, length = 32)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "name_middle", length = 16)
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "name_last", nullable = false, length = 16)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "name_suffix", length = 8)
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "position", length = 16)
	public String getCompanyPosition() {
		return companyPosition;
	}

	public void setCompanyPosition(String companyPosition) {
		this.companyPosition = companyPosition;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = ManagerAssignment.PRIMARY_KEY
			+ ".manager")
	public Set<ManagerAssignment> getAssignedManagers() {
		return assignedManagers;
	}

	public void setAssignedManagers(Set<ManagerAssignment> managers) {
		this.assignedManagers = managers;
	}
}
