package com.cebedo.pmsys.log.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cebedo.pmsys.common.SystemConstants;

//@Service
public class LogServiceImpl {

	public static void main(String[] args) {
		System.out.println(new LogServiceImpl().getLogTree());
	}

	// @Override
	// @Transactional
	public List<File> getLogDirListFromRoot() {
		String root = "C:/Program Files/VCC/PMSYS/system/logs/";
		File rootDir = new File(root);
		ArrayList<File> logDirs = new ArrayList<File>();
		for (File file : rootDir.listFiles()) {
			if (file.getName().equals(SystemConstants.LOGGER_DIR_RESOURCES)) {
				continue;
			}
			if (file.isDirectory()) {
				logDirs.add(file);
			}
		}
		return logDirs;
	}

	/**
	 * Get a specific log file from a list of object logs.
	 * 
	 * @param objectName
	 * @param logName
	 * @return
	 */
	// @Override
	// @Transactional
	public File getLogByObjectAndName(String objectName, String logName) {
		for (File log : listLogsByObject(objectName)) {
			if (logName.equals(log.getName())) {
				return log;
			}
		}
		return null;
	}

	/**
	 * Get the list of logs in this object.
	 * 
	 * @param objectName
	 * @return
	 */
	// @Override
	// @Transactional
	public File[] listLogsByObject(String objectName) {
		// Get list of all log files from the Server filesystem.
		String root = "C:/Program Files/VCC/PMSYS/system/logs/" + objectName;
		File rootDir = new File(root);
		return rootDir.listFiles();
	}

	// @Override
	// @Transactional
	public static List<String> getLogDirListFromRootAsString() {
		String root = "C:/Program Files/VCC/PMSYS/system/logs/";
		File rootDir = new File(root);
		ArrayList<String> logDirsAsString = new ArrayList<String>();
		for (File file : rootDir.listFiles()) {
			if (file.getName().equals(SystemConstants.LOGGER_DIR_RESOURCES)) {
				continue;
			}
			if (file.isDirectory()) {
				logDirsAsString.add(file.getName());
			}
		}
		return logDirsAsString;
	}

	// @Override
	// @Transactional
	public static String getLogTree() {
		String root = "C:/Program Files/VCC/PMSYS/system/logs/";
		File rootDir = new File(root);

		String jsonData = "'data' : [";
		jsonData += "{ 'id' : '" + rootDir.getAbsolutePath()
				+ "', 'parent' : '#', 'text' : 'Logs' },";
		for (File file : rootDir.listFiles()) {
			if (file.getName().equals(SystemConstants.LOGGER_DIR_RESOURCES)) {
				continue;
			}
			if (file.isDirectory()) {
				jsonData += "{ 'id' : '" + file.getAbsolutePath()
						+ "', 'parent' : '"
						+ file.getParentFile().getAbsolutePath()
						+ "', 'text' : '" + file.getName() + "' },";
				for (File logFile : file.listFiles()) {
					jsonData += "{ 'id' : '" + logFile.getAbsolutePath()
							+ "', 'parent' : '"
							+ logFile.getParentFile().getAbsolutePath()
							+ "', 'text' : '" + logFile.getName() + "' },";
				}
			} else {
				jsonData += "{ 'id' : '" + file.getAbsolutePath()
						+ "', 'parent' : '"
						+ file.getParentFile().getAbsolutePath()
						+ "', 'text' : '" + file.getName() + "' },";
			}
		}
		jsonData += "]";
		jsonData = jsonData.replace("},]", "}]");
		return jsonData;
	}

}
