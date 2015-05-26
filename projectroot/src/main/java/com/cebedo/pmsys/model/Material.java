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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = Material.TABLE_NAME)
public class Material implements Serializable {

    public static final String TABLE_NAME = "materials";
    public static final String OBJECT_NAME = "material";
    public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";
    public static final String PROPERTY_ID = "id";
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String description;
    private MaterialCollection collection;
    private double pricePerPiece;
    private Date purchaseDate;
    private Supplier supplier;
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

    @Column(name = "price_per_piece", nullable = false)
    public double getPricePerPiece() {
	return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
	this.pricePerPiece = pricePerPiece;
    }

    @Column(name = "purchase_date", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getPurchaseDate() {
	return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
	this.purchaseDate = purchaseDate;
    }

    @ManyToOne
    @JoinColumn(name = MaterialCollection.COLUMN_PRIMARY_KEY)
    public MaterialCollection getCollection() {
	return collection;
    }

    public void setCollection(MaterialCollection collection) {
	this.collection = collection;
    }

    @OneToOne(mappedBy = "material")
    public Supplier getSupplier() {
	return supplier;
    }

    public void setSupplier(Supplier supplier) {
	this.supplier = supplier;
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
