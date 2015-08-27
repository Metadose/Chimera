package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;

import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Project;

@Embeddable
public class FieldAssignmentID implements Serializable {

    private static final long serialVersionUID = 8218590771672884881L;
    private Project project;
    private Field field;
    private String label;
    private String value;

    public FieldAssignmentID() {
	;
    }

    public FieldAssignmentID(Project proj, Field field2) {
	setProject(proj);
	setField(field2);
    }

    @ManyToOne
    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    @ManyToOne
    public Field getField() {
	return field;
    }

    public void setField(Field field) {
	this.field = field;
    }

    @Column(name = "label", nullable = false, length = 32)
    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = StringUtils.trim(label);
    }

    @Column(name = "value", nullable = false, length = 255)
    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = StringUtils.trim(value);
    }

}
