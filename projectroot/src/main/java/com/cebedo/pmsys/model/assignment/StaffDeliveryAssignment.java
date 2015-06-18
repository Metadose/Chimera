package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.model.DeliveryToDelete;
import com.cebedo.pmsys.model.Staff;

@Entity
@Table(name = StaffDeliveryAssignment.TABLE_NAME)
public class StaffDeliveryAssignment implements Serializable {

    private static final long serialVersionUID = 2283380966185220254L;

    public static final String TABLE_NAME = "assignments_staff_delivery";

    private long staffID;
    private long deliveryID;

    @Id
    @Column(name = Staff.COLUMN_PRIMARY_KEY, nullable = false)
    public long getStaffID() {
	return staffID;
    }

    public void setStaffID(long staffID) {
	this.staffID = staffID;
    }

    @Id
    @Column(name = DeliveryToDelete.COLUMN_PRIMARY_KEY, nullable = false)
    public long getDeliveryID() {
	return deliveryID;
    }

    public void setDeliveryID(long deliveryID) {
	this.deliveryID = deliveryID;
    }

}
