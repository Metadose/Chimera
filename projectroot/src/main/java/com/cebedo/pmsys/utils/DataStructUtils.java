package com.cebedo.pmsys.utils;

import java.util.ArrayList;
import java.util.List;

public class DataStructUtils {

    public static List<Long> convertArrayToList(long[] longNums) {
	List<Long> longNumbers = new ArrayList<Long>();
	for (long longNum : longNums) {
	    longNumbers.add(longNum);
	}
	return longNumbers;
    }

}
