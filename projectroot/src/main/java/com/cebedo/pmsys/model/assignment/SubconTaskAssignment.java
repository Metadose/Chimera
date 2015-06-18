package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

//@Entity
//@Table(name = SubconTaskAssignment.TABLE_NAME)
public class SubconTaskAssignment implements Serializable {

    private static final long serialVersionUID = -178782848811230216L;

    public static final String TABLE_NAME = "assignments_subcon_task";

    private long taskID;
    private long subconID;

    // @Id
    // @Column(name = Task.COLUMN_PRIMARY_KEY, nullable = false)
    public long getTaskID() {
	return taskID;
    }

    public void setTaskID(long taskID) {
	this.taskID = taskID;
    }

    // @Id
    // @Column(name = Subcontractor.COLUMN_PRIMARY_KEY, nullable = false)
    public long getSubcontractorID() {
	return subconID;
    }

    public void setSubcontractorID(long subconID) {
	this.subconID = subconID;
    }
}
