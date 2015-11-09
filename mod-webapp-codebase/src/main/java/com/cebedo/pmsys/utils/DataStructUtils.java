package com.cebedo.pmsys.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.model.Staff;

/**
 * Use Google Guava instead.
 */
@Deprecated
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

    public static Object[] addElemToArray(Object[] elems, Object elem) {
	List<Object> stringList = convertArrayToList(elems);
	stringList.add(elem);
	return (Object[]) stringList.toArray();
    }

    public static List<Object> convertArrayToList(Object[] strings) {
	return Arrays.asList(strings);
    }

    public static long[] toPrimitives(Long... objects) {
	long[] primitives = new long[objects.length];
	for (int i = 0; i < objects.length; i++) {
	    primitives[i] = objects[i];
	}
	return primitives;
    }

    public static long[] toPrimitives(List<Long> objects) {
	long[] primitives = new long[objects.size()];
	for (int i = 0; i < objects.size(); i++) {
	    primitives[i] = objects.get(i);
	}
	return primitives;
    }

    public static long[] addElemToArray(long[] elems, long elem) {
	List<Long> elemList = convertArrayToList(elems);
	elemList.add(elem);
	return toPrimitives(elemList);
    }

}
