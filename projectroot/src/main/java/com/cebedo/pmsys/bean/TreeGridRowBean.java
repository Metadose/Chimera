package com.cebedo.pmsys.bean;

public class TreeGridRowBean {

    private long primaryKey;
    private long foreignKey;
    private String name;
    private String value;

    public TreeGridRowBean() {
	;
    }

    public TreeGridRowBean(long pKey, long fKey, String n, String v) {
	setPrimaryKey(pKey);
	setForeignKey(fKey);
	setName(n);
	setValue(v);
    }

    public long getPrimaryKey() {
	return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
	this.primaryKey = primaryKey;
    }

    public long getForeignKey() {
	return foreignKey;
    }

    public void setForeignKey(long foreignKey) {
	this.foreignKey = foreignKey;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
