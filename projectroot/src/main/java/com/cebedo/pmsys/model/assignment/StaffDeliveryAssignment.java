package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.model.Delivery;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

@Entity
@Table(name = StaffDeliveryAssignment.TABLE_NAME)
public class StaffDeliveryAssignment implements Serializable {

    public static final String TABLE_NAME = "assignments_staff_delivery";
    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("StaffDeliveryAssignment");

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
    @Column(name = Delivery.COLUMN_PRIMARY_KEY, nullable = false)
    public long getDeliveryID() {
	return deliveryID;
    }

    public void setDeliveryID(long deliveryID) {
	this.deliveryID = deliveryID;
    }

}
