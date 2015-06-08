package com.cebedo.pmsys.helper;

import java.util.ArrayList;
import java.util.List;

public class DataStructHelper {

    public static List<Long> convertArrayToList(long[] longNums) {
	List<Long> longNumbers = new ArrayList<Long>();
	for (long longNum : longNums) {
	    longNumbers.add(longNum);
	}
	return longNumbers;
    }

}
