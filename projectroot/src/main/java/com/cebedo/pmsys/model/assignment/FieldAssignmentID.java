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

    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof FieldAssignmentID)) {
	    return false;
	}

	long projectID = getProject().getId();
	String label = getLabel();
	String value = getValue();

	FieldAssignmentID comparedObj = (FieldAssignmentID) obj;
	long projectIDcompare = comparedObj.getProject().getId();
	String labelCompare = comparedObj.getLabel();
	String valueCompare = comparedObj.getValue();

	boolean projectIDEqual = projectID == projectIDcompare;
	boolean labelEqual = label.equals(labelCompare);
	boolean valueEqual = value.equals(valueCompare);

	return projectIDEqual && labelEqual && valueEqual;
    }

    @Override
    public int hashCode() {
	int projectID = ((Long) getProject().getId()).hashCode();
	int label = getLabel().hashCode();
	int value = getValue().hashCode();
	return projectID + label + value;
    }

}
