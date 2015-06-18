package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = MaterialToRemove.TABLE_NAME)
public class MaterialToRemove implements Serializable {

    private static final long serialVersionUID = -7556543678779459364L;
    public static final String TABLE_NAME = "materials";
    public static final String OBJECT_NAME = "material";
    public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_PROJECT = "project";

    public static final String MATERIALS_SUMMARY_KEY_TOTAL_COST = "totalCostOfMaterials";
    public static final String MATERIALS_SUMMARY_KEY_COUNT = "materialsCountMap";

    private long id;
    private String name;
    private String description;
    private double price;
    private Date purchaseDatetime;
    private int status;
    private Supplier supplier;
    private DeliveryToDelete delivery;
    private Storage storage;
    private Project project;
    private Task task;
    private Company company;
    private Set<Expense> expenses;

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

    @Column(name = "price", nullable = false)
    public double getPrice() {
	return price;
    }

    public void setPrice(double pricePerPiece) {
	this.price = pricePerPiece;
    }

    @Column(name = "purchase_datetime", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getPurchaseDatetime() {
	return purchaseDatetime;
    }

    public void setPurchaseDatetime(Date purchaseDatetime) {
	this.purchaseDatetime = purchaseDatetime;
    }

    @ManyToOne
    @JoinColumn(name = Supplier.COLUMN_PRIMARY_KEY)
    public Supplier getSupplier() {
	return supplier;
    }

    public void setSupplier(Supplier supplier) {
	this.supplier = supplier;
    }

    @Column(name = "status", nullable = false, length = 2)
    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = Project.COLUMN_PRIMARY_KEY)
    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    @ManyToOne
    @JoinColumn(name = Task.COLUMN_PRIMARY_KEY)
    public Task getTask() {
	return task;
    }

    public void setTask(Task task) {
	this.task = task;
    }

    @ManyToOne
    @JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @ManyToOne
    @JoinColumn(name = Storage.COLUMN_PRIMARY_KEY)
    public Storage getStorage() {
	return storage;
    }

    public void setStorage(Storage storage) {
	this.storage = storage;
    }

    @ManyToOne
    @JoinColumn(name = DeliveryToDelete.COLUMN_PRIMARY_KEY)
    public DeliveryToDelete getDelivery() {
	return delivery;
    }

    public void setDelivery(DeliveryToDelete delivery) {
	this.delivery = delivery;
    }

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
    public Set<Expense> getExpenses() {
	return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
	this.expenses = expenses;
    }
}
