package com.cebedo.pmsys.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

public class LogHelper {

	private FileHelper fileHelper = new FileHelper();

	public String getLogContents(String logPath) {
		try {
			return this.fileHelper.getFileContents(logPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Generate a tree json data for the logs.
	 * 
	 * @return
	 */
	public String getLogTree(String rootPath) {
		String root = rootPath + "/system/logs/";
		File rootDir = new File(root);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		String jsonData = "{'data' : [";
		String rootDirID = rootDir.getAbsolutePath().replace("\\", "/");
		jsonData += "{ 'id' : '"
				+ rootDirID
				+ "', 'parent' : '#', 'text' : 'Logs', 'icon': 'fa fa-angle-right' },";

		for (File file : rootDir.listFiles()) {

			if (file.isDirectory()) {
				String id = file.getAbsolutePath().replace("\\", "/");
				String parentID = file.getParentFile().getAbsolutePath()
						.replace("\\", "/");

				jsonData += "{ 'id' : '" + id + "', 'parent' : '" + parentID
						+ "', 'text' : '" + file.getName()
						+ "', 'icon': 'fa fa-folder-open-o' },";

				for (File logFile : file.listFiles()) {

					String logFileID = logFile.getAbsolutePath().replace("\\",
							"/");
					String logFileParentID = logFile.getParentFile()
							.getAbsolutePath().replace("\\", "/");

					jsonData += "{ 'id' : '" + logFileID + "', 'parent' : '"
							+ logFileParentID + "', 'text' : '"
							+ logFile.getName() + " ("
							+ (logFile.length() / 1024) + "KB) "
							+ sdf.format(logFile.lastModified())
							+ "', 'icon': 'fa fa-file-o' },";
				}
			} else {
				String id = file.getAbsolutePath().replace("\\", "/");
				String parentID = file.getParentFile().getAbsolutePath()
						.replace("\\", "/");

				jsonData += "{ 'id' : '" + id + "', 'parent' : '" + parentID
						+ "', 'text' : '" + file.getName()
						+ "', 'icon': 'fa fa-file-o' },";
			}
		}
		jsonData += "]}";
		jsonData = jsonData.replace("},]", "}]");
		return jsonData;
	}

}
