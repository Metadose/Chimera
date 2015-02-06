package com.cebedo.pmsys.field.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = Field.TABLE_NAME)
public class Field implements Serializable {

	public static final String TABLE_NAME = "fields";

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String type;

}
