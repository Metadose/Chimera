package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;

//@Entity
//@Table(name = Expense.TABLE_NAME)
public class Expense implements Serializable {

	public static final String OBJECT_NAME = "subcontractor";
	public static final String TABLE_NAME = "subcontractors";
	public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";
	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private String description;
	private float value;
	private Date date;
	private Company company;
	private Set<Project> projects;
	private Set<Task> tasks;
	private Set<Team> team;
	private Set<Staff> members;
	private Set<Photo> photos;
	private Set<ProjectFile> files;
	private Set<Subcontractor> subcontractor;

	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// @Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// @Column(name = "name", nullable = false, length = 64)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "value", nullable = false)
	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public Set<Team> getTeam() {
		return team;
	}

	public void setTeam(Set<Team> team) {
		this.team = team;
	}

	public Set<Staff> getMembers() {
		return members;
	}

	public void setMembers(Set<Staff> members) {
		this.members = members;
	}

	public Set<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}

	public Set<ProjectFile> getFiles() {
		return files;
	}

	public void setFiles(Set<ProjectFile> files) {
		this.files = files;
	}

	// @Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// @Column(name = "date", nullable = false)
	// @Temporal(TemporalType.DATE)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	// @ManyToMany
	// @JoinTable(name = SubconExpenseAssignment.TABLE_NAME, joinColumns = {
	// @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = {
	// @JoinColumn(name = Subcontractor.COLUMN_PRIMARY_KEY, nullable = false,
	// updatable = false) })
	public Set<Subcontractor> getSubcontractor() {
		return subcontractor;
	}

	public void setSubcontractor(Set<Subcontractor> subcontractor) {
		this.subcontractor = subcontractor;
	}
}
