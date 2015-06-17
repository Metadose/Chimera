package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.utils.NumberFormatUtils;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

public class Delivery implements IDomainObject {

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("Delivery");

    /**
     * Key:
     * company:13:project:123:delivery:datetime:2015.12.31:14.22:uuid:123123
     */
    private Company company;
    private Project project;
    private Date datetime;
    private UUID uuid;

    /**
     * Details.
     */
    private String name;
    private String description;

    /**
     * More details.
     */
    private double grandTotalOfMaterials;
    private Set<Material> materials;
    private Set<Staff> staff;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public Delivery() {
	;
    }

    public Delivery(Project proj) {
	setCompany(proj.getCompany());
	setProject(proj);
    }

    @Override
    public Map<String, Object> getExtMap() {
	return extMap;
    }

    @Override
    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	// company:13:project:123:delivery:datetime:2015.12.31:14.22:uuid:123123
	String companyPart = Company.OBJECT_NAME + ":" + this.company.getId()
		+ ":";
	String projectPart = Project.OBJECT_NAME + ":" + this.project.getId()
		+ ":";
	String deliveryPart = RedisConstants.OBJECT_CONVERSATION + ":";
	String dateStr = DateUtils.formatDate(this.datetime,
		"yyyy.MM.dd:hh.mm.ss");
	String datetimePart = "datetime:" + dateStr + ":";
	String uuidPart = "uuid:" + this.uuid;

	String key = companyPart + projectPart + deliveryPart + datetimePart
		+ uuidPart;
	return key;
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    public Date getDatetime() {
	return datetime;
    }

    public void setDatetime(Date datetime) {
	this.datetime = datetime;
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Set<Material> getMaterials() {
	return materials;
    }

    public void setMaterials(Set<Material> materials) {
	this.materials = materials;
    }

    public String getGrandTotalOfMaterialsAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(
		grandTotalOfMaterials);
    }

    public double getGrandTotalOfMaterials() {
	return grandTotalOfMaterials;
    }

    public void setGrandTotalOfMaterials(double grandTotalOfMaterials) {
	this.grandTotalOfMaterials = grandTotalOfMaterials;
    }

    public Set<Staff> getStaff() {
	return staff;
    }

    public void setStaff(Set<Staff> staff) {
	this.staff = staff;
    }

    public static String constructKey(Project project, Date datetime, UUID uuid) {
	Company company = project.getCompany();
	String companyPart = Company.OBJECT_NAME + ":" + company.getId() + ":";
	String projectPart = Project.OBJECT_NAME + ":" + project.getId() + ":";
	String deliveryPart = RedisConstants.OBJECT_CONVERSATION + ":";
	String dateStr = DateUtils.formatDate(datetime, "yyyy.MM.dd:hh.mm.ss");
	String datetimePart = "datetime:" + dateStr + ":";
	String uuidPart = "uuid:" + uuid;
	String key = companyPart + projectPart + deliveryPart + datetimePart
		+ uuidPart;
	return key;
    }

    public static String constructPattern(Project project) {
	Company company = project.getCompany();
	String companyPart = Company.OBJECT_NAME + ":" + company.getId() + ":";
	String projectPart = Project.OBJECT_NAME + ":" + project.getId() + ":";
	String deliveryPart = RedisConstants.OBJECT_CONVERSATION + ":";
	String datetimePart = "datetime:*";
	String uuidPart = "uuid:*";
	String key = companyPart + projectPart + deliveryPart + datetimePart
		+ uuidPart;
	return key;
    }

    public static String constructPattern(String uuid2) {
	String companyPart = Company.OBJECT_NAME + ":*:";
	String projectPart = Project.OBJECT_NAME + ":*:";
	String deliveryPart = RedisConstants.OBJECT_CONVERSATION + ":";
	String datetimePart = "datetime:*";
	String uuidPart = "uuid:" + uuid2;
	String key = companyPart + projectPart + deliveryPart + datetimePart
		+ uuidPart;
	return key;
    }

}
