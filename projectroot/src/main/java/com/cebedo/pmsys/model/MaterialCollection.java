package com.cebedo.pmsys.model;

import java.io.Serializable;
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

@Entity
@Table(name = MaterialCollection.TABLE_NAME)
public class MaterialCollection implements Serializable {

    public static final String TABLE_NAME = "material_collections";
    public static final String OBJECT_NAME = "materialcollection";
    public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";
    public static final String PROPERTY_ID = "id";
    private static final long serialVersionUID = 1L;

    private long id;
    private Set<Material> materials;
    private int quantity;
    private double priceAsTotal;
    private Storage storage;
    private Task task;
    private Company company;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    public Set<Material> getMaterials() {
	return materials;
    }

    public void setMaterials(Set<Material> materials) {
	this.materials = materials;
    }

    @Column(name = "quantity", nullable = false)
    public int getQuantity() {
	return quantity;
    }

    public void setQuantity(int quantity) {
	this.quantity = quantity;
    }

    @Column(name = "price_as_total", nullable = false)
    public double getPriceAsTotal() {
	return priceAsTotal;
    }

    public void setPriceAsTotal(double priceAsTotal) {
	this.priceAsTotal = priceAsTotal;
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
    @JoinColumn(name = Task.COLUMN_PRIMARY_KEY)
    public Task getTask() {
	return task;
    }

    public void setTask(Task task) {
	this.task = task;
    }

}
