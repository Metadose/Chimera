package com.cebedo.pmsys.model.assignment;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cebedo.pmsys.base.IObjectAssignment;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Project;

@Entity
@Table(name = FieldAssignment.TABLE_NAME)
@AssociationOverrides({
	@AssociationOverride(name = FieldAssignment.PRIMARY_KEY
		+ ".project", joinColumns = @JoinColumn(name = Project.COLUMN_PRIMARY_KEY) ),
	@AssociationOverride(name = FieldAssignment.PRIMARY_KEY
		+ ".field", joinColumns = @JoinColumn(name = Field.COLUMN_PRIMARY_KEY) ) })
public class FieldAssignment implements IObjectAssignment {

    private static final long serialVersionUID = -6885137480392887691L;
    public static final String OBJECT_NAME = "FieldAssignment";
    public static final String OBJECT_LABEL = "Field Assignment";
    public static final String TABLE_NAME = "assignments_project_field";
    public static final String PRIMARY_KEY = "assignmentID";

    private FieldAssignmentID assignmentID = new FieldAssignmentID();

    @EmbeddedId
    public FieldAssignmentID getAssignmentID() {
	return assignmentID;
    }

    public void setAssignmentID(FieldAssignmentID assignmentID) {
	this.assignmentID = assignmentID;
    }

    @Transient
    public Project getProject() {
	return getAssignmentID().getProject();
    }

    public void setProject(Project project) {
	getAssignmentID().setProject(project);
    }

    @Transient
    public Field getField() {
	return getAssignmentID().getField();
    }

    public void setField(Field field) {
	getAssignmentID().setField(field);
    }

    @Transient
    public String getLabel() {
	return getAssignmentID().getLabel();
    }

    public void setLabel(String str) {
	getAssignmentID().setLabel(str);
    }

    @Transient
    public String getValue() {
	return getAssignmentID().getValue();
    }

    public void setValue(String str) {
	getAssignmentID().setValue(str);
    }

    @Transient
    @Override
    public String getName() {
	return getLabel();
    }

    @Transient
    @Override
    public String getObjectName() {
	return OBJECT_NAME;
    }

    @Transient
    @Override
    public String getTableName() {
	return TABLE_NAME;
    }

    public FieldAssignment clone() {
	try {
	    return (FieldAssignment) super.clone();
	} catch (Exception e) {
	    return null;
	}
    }
}
