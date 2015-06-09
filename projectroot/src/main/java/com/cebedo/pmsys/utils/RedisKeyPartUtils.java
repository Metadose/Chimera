package com.cebedo.pmsys.utils;

public class RedisKeyPartUtils {

    public static String generateKeyPartWithWildcard(String objectName, Long id) {
	String idPart = id == null ? "*" : id.toString();
	String keyPart = objectName + ":" + idPart + ":";
	return keyPart;
    }

    public static String generateKeyPartWithWildcard(String objectName,
	    Integer id) {
	String idPart = id == null ? "*" : id.toString();
	String keyPart = objectName + ":" + idPart + ":";
	return keyPart;
    }

    public static String generateKeyPartWithWildcard(String objectName,
	    String id) {
	String idPart = id == null ? "*" : id.toString();
	String keyPart = objectName + ":" + idPart + ":";
	return keyPart;
    }

}