package com.cebedo.pmsys.log.service;

import java.io.File;
import java.util.List;

public interface LogService {

	public List<String> getLogDirListFromRootAsString();

	public List<File> getLogDirListFromRoot();

	public File getLogByObjectAndName(String objectName, String logName);

	public File[] listLogsByObject(String objectName);

	public String getLogTree();
}
