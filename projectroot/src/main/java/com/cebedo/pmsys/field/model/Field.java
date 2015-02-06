package com.cebedo.pmsys.field.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = Field.tableName)
public class Field implements Serializable {

	public static final String tableName = "fields";

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String type;

}
