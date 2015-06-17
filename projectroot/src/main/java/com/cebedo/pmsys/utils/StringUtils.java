package com.cebedo.pmsys.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static List<Integer> getAllIndicesOfSubstring(String word,
	    String guess) {
	List<Integer> indexList = new ArrayList<Integer>();
	int index = word.indexOf(guess);
	while (index >= 0) {
	    indexList.add(index);
	    index = word.indexOf(guess, index + 1);
	}
	return indexList;
    }

}
