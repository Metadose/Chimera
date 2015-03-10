package com.cebedo.pmsys.staff.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StaffWrapper {

	private final Staff staff;

	public StaffWrapper(Staff stf) {
		this.staff = stf;
	}

	public Staff getStaff() {
		return staff;
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
}
