package com.cebedo.pmsys.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;

public class StaffWrapper {

    private final Staff staff;

    public StaffWrapper(Staff stf) {
	this.staff = stf;
    }

    public Staff getStaff() {
	return staff;
    }

    /**
     * Remove staff items without names.
     * 
     * @param wrappedStaffList
     * @return
     */
    public static List<StaffWrapper> removeEmptyNames(
	    List<StaffWrapper> wrappedStaffList) {
	int i = 0;
	List<Integer> toRemove = new ArrayList<Integer>();
	for (StaffWrapper wrappedStaff : wrappedStaffList) {
	    Staff wrpStf = wrappedStaff.getStaff();
	    if (wrpStf.getFullName().isEmpty()) {
		toRemove.add(i);
	    }
	    i++;
	}
	int removedIndices = 0;
	for (int index : toRemove) {
	    wrappedStaffList.remove(index - removedIndices);
	    removedIndices++;
	}
	return wrappedStaffList;
    }

    public static List<StaffWrapper> wrap(List<Staff> staffList) {
	List<StaffWrapper> wrappedList = new ArrayList<StaffWrapper>();
	for (Staff staff : staffList) {
	    wrappedList.add(new StaffWrapper(staff));
	}
	return wrappedList;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof StaffWrapper
		&& this.staff.getId() == (((StaffWrapper) obj).staff.getId());
    }

    @Override
    public int hashCode() {
	return staff.hashCode();
    }

    public static List<StaffWrapper> wrap(Set<ManagerAssignment> staffList) {
	List<StaffWrapper> wrappedList = new ArrayList<StaffWrapper>();
	for (ManagerAssignment assignment : staffList) {
	    wrappedList.add(new StaffWrapper(assignment.getManager()));
	}
	return wrappedList;
    }

    public static List<Staff> unwrap(List<StaffWrapper> wrappedStaffList) {
	List<Staff> unwrappedList = new ArrayList<Staff>();
	for (StaffWrapper wrappedStaff : wrappedStaffList) {
	    unwrappedList.add(wrappedStaff.getStaff());
	}
	return unwrappedList;
    }

    public static List<StaffWrapper> wrapSet(Set<Staff> staffList) {
	List<StaffWrapper> wrappedList = new ArrayList<StaffWrapper>();
	for (Staff staff : staffList) {
	    wrappedList.add(new StaffWrapper(staff));
	}
	return wrappedList;
    }
}
