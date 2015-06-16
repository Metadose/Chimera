package com.cebedo.pmsys.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.cebedo.pmsys.model.Staff;

public class DataStructUtils {

    public static List<Long> convertArrayToList(long[] longNums) {
	List<Long> longNumbers = new ArrayList<Long>();
	for (long longNum : longNums) {
	    longNumbers.add(longNum);
	}
	return longNumbers;
    }

    public static List<Staff> convertSetToList(Set<Staff> staffSet) {
	List<Staff> staffList = new ArrayList<Staff>();
	staffList.addAll(staffSet);
	return staffList;
    }

}
