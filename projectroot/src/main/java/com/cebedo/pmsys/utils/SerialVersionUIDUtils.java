package com.cebedo.pmsys.utils;

public class SerialVersionUIDUtils {

    public static long convertStringToLong(String str) {
	long total = 0;
	for (char character : str.toCharArray()) {
	    long ascii = (int) character;
	    total += ascii;
	}
	return total;
    }

}
