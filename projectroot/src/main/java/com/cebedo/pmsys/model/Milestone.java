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
import javax.persistence.Table;

@Entity
@Table(name = Milestone.TABLE_NAME)
public class Milestone implements Serializable {

    private static final long serialVersionUID = -2433583059419570548L;
    public static final String OBJECT_NAME = "milestone";
    public static final String TABLE_NAME = "milestones";
    public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";
    public static final String PROPERTY_ID = "id";

    private long id;
    private String name;
    private String description;
    private Project project;
    private Set<Task> tasks;
    private Company company;

    public Milestone() {
	;
    }

    public Milestone(Company company2, Project project2, String milestoneName) {
	setCompany(company2);
	setName(milestoneName);
	setProject(project2);
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

    @Column(name = "name", nullable = false, length = 32)
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = Project.COLUMN_PRIMARY_KEY)
    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    @OneToMany(mappedBy = "milestone")
    public Set<Task> getTasks() {
	return tasks;
    }

    public void setTasks(Set<Task> tasks) {
	this.tasks = tasks;
    }

    @ManyToOne
    @JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof Milestone ? ((Milestone) obj).getId() == (getId()) : false;
    }

    @Override
    public int hashCode() {
	return ((Long) getId()).hashCode();
    }

}
