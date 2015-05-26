package com.cebedo.pmsys.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = Supplier.TABLE_NAME)
public class Supplier implements Serializable {

    public static final String TABLE_NAME = "suppliers";
    public static final String OBJECT_NAME = "supplier";
    public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";
    public static final String PROPERTY_ID = "id";
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String description;
    private Material material;
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

    @OneToOne
    @JoinColumn(name = Material.COLUMN_PRIMARY_KEY)
    public Material getMaterial() {
	return material;
    }

    public void setMaterial(Material material) {
	this.material = material;
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
