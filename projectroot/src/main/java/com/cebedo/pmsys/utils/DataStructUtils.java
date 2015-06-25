package com.cebedo.pmsys.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.model.Staff;

public class DataStructUtils {

    public static List<Long> convertArrayToList(long[] longNums) {
	List<Long> longNumbers = new ArrayList<Long>();
	for (long longNum : longNums) {
	    longNumbers.add(longNum);
	}
	return longNumbers;
    }

    public static Set<Material> convertListToSet(List<Material> myList) {
	Set<Material> mySet = new HashSet<Material>();
	mySet.addAll(myList);
	return mySet;
    }

    public static List<Staff> convertSetToList(Set<Staff> staffSet) {
	List<Staff> staffList = new ArrayList<Staff>();
	staffList.addAll(staffSet);
	return staffList;
    }

    public static Collection<String> convertArrayToList(
	    String[] estimationToCompute) {
	List<String> strList = new ArrayList<String>();
	for (String str : estimationToCompute) {
	    strList.add(str);
	}
	return strList;
    }

}
