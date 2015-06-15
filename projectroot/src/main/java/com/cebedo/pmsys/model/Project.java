package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cebedo.pmsys.enums.ProjectStatus;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.model.assignment.ProjectStaffAssignment;
import com.cebedo.pmsys.model.assignment.TeamAssignment;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

@Entity
@Table(name = Project.TABLE_NAME)
public class Project implements Serializable {

    public static final String OBJECT_NAME = "project";
    public static final String TABLE_NAME = "projects";

    public static final String COLUMN_PRIMARY_KEY = "project_id";
    public static final String COLUMN_NAME = "name";

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";

    public static final String SUB_MODULE_PROFILE = "profile";

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("Project");

    private long id;
    private String name;
    private int type;
    private int status;
    private Set<ManagerAssignment> managerAssignments;
    private Set<Team> assignedTeams;
    private Set<FieldAssignment> assignedFields;
    private Set<Staff> assignedStaff;
    private String thumbnailURL;
    private String location;
    private String notes;
    private Set<Milestone> milestones;
    private Set<Task> assignedTasks;
    private Set<ProjectFile> files;
    private Set<Photo> photos;
    private Company company;
    private Set<Delivery> deliveries;
    private Set<Reminder> reminders;
    private Set<Material> materials;
    private Set<Expense> expenses;

    /**
     * Bean-backed forms.
     */
    private long[] staffIDs;

    // private Set<Subcontractor> subcontractor;

    public Project() {
	;
    }

    public Project(long id) {
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

    @Column(name = "name", nullable = false, length = 32)
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Column(name = "type", nullable = false, length = 2)
    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }

    @Column(name = "status", nullable = false, length = 2)
    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    @Transient
    public ProjectStatus getStatusEnum() {
	return ProjectStatus.of(getStatus());
    }

    @OrderBy(Project.COLUMN_PRIMARY_KEY)
    @OneToMany(mappedBy = FieldAssignment.PRIMARY_KEY + ".project", cascade = CascadeType.REMOVE)
    public Set<FieldAssignment> getAssignedFields() {
	return assignedFields;
    }

    public void setAssignedFields(Set<FieldAssignment> fields) {
	this.assignedFields = fields;
    }

    /**
     * Project to Staff with extra columns.
     */
    @OneToMany(mappedBy = ManagerAssignment.PRIMARY_KEY + ".project", cascade = CascadeType.REMOVE)
    public Set<ManagerAssignment> getManagerAssignments() {
	return this.managerAssignments;
    }

    public void setManagerAssignments(Set<ManagerAssignment> man) {
	this.managerAssignments = man;
    }

    /**
     * Project to Team many-to-many without extra columns.
     */
    @ManyToMany
    @JoinTable(name = TeamAssignment.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Team.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
    public Set<Team> getAssignedTeams() {
	return this.assignedTeams;
    }

    @Transient
    public Set<Team> getAssignedProjectBasedTeams() {
	Set<Team> teams = new HashSet<Team>();
	for (Team team : this.assignedTeams) {
	    if (team.isProjectBased()) {
		teams.add(team);
	    }
	}
	return teams;
    }

    @ManyToMany
    @JoinTable(name = ProjectStaffAssignment.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Staff.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
    public Set<Staff> getAssignedStaff() {
	return assignedStaff;
    }

    public void setAssignedStaff(Set<Staff> assignedStaff) {
	this.assignedStaff = assignedStaff;
    }

    @Transient
    public long[] getStaffIDs() {
	return staffIDs;
    }

    public void setStaffIDs(long[] staffIDs) {
	this.staffIDs = staffIDs;
    }

    public void setAssignedTeams(Set<Team> teams) {
	this.assignedTeams = teams;
    }

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<Milestone> getMilestones() {
	return milestones;
    }

    public void setMilestones(Set<Milestone> milestones) {
	this.milestones = milestones;
    }

    /**
     * Project to Task many-to-many without extra columns.
     */
    @OrderBy(Task.COLUMN_DATE_START)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<Task> getAssignedTasks() {
	return assignedTasks;
    }

    public void setAssignedTasks(Set<Task> assignedTasks) {
	this.assignedTasks = assignedTasks;
    }

    @Column(name = "thumbnail_url", length = 255)
    public String getThumbnailURL() {
	return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
	this.thumbnailURL = thumbnailURL;
    }

    @Column(name = "location", length = 108)
    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    @Column(name = "notes")
    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<ProjectFile> getFiles() {
	return files;
    }

    public void setFiles(Set<ProjectFile> files) {
	this.files = files;
    }

    @OrderBy(Photo.COLUMN_PRIMARY_KEY)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<Photo> getPhotos() {
	return photos;
    }

    public void setPhotos(Set<Photo> photos) {
	this.photos = photos;
    }

    @Override
    public String toString() {
	return "[Project] " + getId() + " [Name] " + getName();
    }

    @ManyToOne
    @JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<Delivery> getDeliveries() {
	return deliveries;
    }

    public void setDeliveries(Set<Delivery> deliveries) {
	this.deliveries = deliveries;
    }

    @OneToMany(mappedBy = "project")
    public Set<Material> getMaterials() {
	return materials;
    }

    public void setMaterials(Set<Material> materials) {
	this.materials = materials;
    }

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<Expense> getExpenses() {
	return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
	this.expenses = expenses;
    }

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<Reminder> getReminders() {
	return reminders;
    }

    public void setReminders(Set<Reminder> reminders) {
	this.reminders = reminders;
    }

    // @ManyToMany
    // @JoinTable(name = SubconProjectAssignment.TABLE_NAME, joinColumns = {
    // @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = {
    // @JoinColumn(name = Subcontractor.COLUMN_PRIMARY_KEY, nullable = false,
    // updatable = false) })
    // public Set<Subcontractor> getSubcontractor() {
    // return subcontractor;
    // }
    //
    // public void setSubcontractor(Set<Subcontractor> subcontractor) {
    // this.subcontractor = subcontractor;
    // }
}