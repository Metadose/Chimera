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

@Entity
@Table(name = Photo.TABLE_NAME)
public class Photo implements Serializable {

    private static final long serialVersionUID = 1177082952071817759L;
    public static final String OBJECT_NAME = "photo";
    public static final String TABLE_NAME = "photos";
    public static final String COLUMN_PRIMARY_KEY = "photo_id";

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";

    private long id;
    private String name;
    private String description;
    private Date dateUploaded;
    private Staff uploader;
    private long size;
    private String location;
    private Project project;
    private Company company;

    public Photo() {
	;
    }

    public Photo(Staff staff, String fileLocation, Project proj,
	    String originalFilename, String descript, long size, Date uploaded,
	    Company company) {
	this.setUploader(staff);
	this.setLocation(fileLocation);
	this.setProject(proj);
	this.setName(originalFilename);
	this.setDescription(descript);
	this.setSize(size);
	this.setDateUploaded(uploaded);
	this.setCompany(company);
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

    @Column(name = "description", length = 255)
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Column(name = "size", nullable = false)
    public long getSize() {
	return size;
    }

    public void setSize(long size) {
	this.size = size;
    }

    @Column(name = "location", nullable = false, length = 255)
    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    @ManyToOne
    @JoinColumn(name = Staff.COLUMN_PRIMARY_KEY)
    public Staff getUploader() {
	return uploader;
    }

    public void setUploader(Staff uploader) {
	this.uploader = uploader;
    }

    @ManyToOne
    @JoinColumn(name = Project.COLUMN_PRIMARY_KEY)
    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    @Column(name = "date_uploaded", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateUploaded() {
	return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
	this.dateUploaded = dateUploaded;
    }

    @ManyToOne
    @JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

}
