package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cebedo.pmsys.model.assignment.FieldAssignment;

/**
 * TODO Update this whole class. Introduce UUID's as primary keys.
 */
@Deprecated
@Entity
@Table(name = Field.TABLE_NAME)
public class Field implements Serializable {

    private static final long serialVersionUID = -3047038623597170285L;
    public static final String TABLE_NAME = "fields";
    public static final String OBJECT_NAME = "field";
    public static final String IDENTIFIER_SEPARATOR = "-3edc-";

    public static final String COLUMN_PRIMARY_KEY = "field_id";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_VALUE = "value";

    public static final String PROPERTY_ID = "id";

    private long id;
    private String name;
    private Set<FieldAssignment> fieldAssignments;

    public Field() {
	;
    }

    public Field(long fieldID) {
	setId(fieldID);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = true)
    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    @Column(name = "name", unique = true)
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @OneToMany(mappedBy = FieldAssignment.PRIMARY_KEY + ".field", cascade = CascadeType.REMOVE)
    public Set<FieldAssignment> getFieldAssignments() {
	return fieldAssignments;
    }

    public void setFieldAssignments(Set<FieldAssignment> fieldAssignments) {
	this.fieldAssignments = fieldAssignments;
    }

}
