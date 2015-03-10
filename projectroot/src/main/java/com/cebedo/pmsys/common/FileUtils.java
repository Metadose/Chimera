package com.cebedo.pmsys.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public abstract class FileUtils {

	/**
	 * Upload a file.
	 * 
	 * @param file
	 * @param id
	 * @param objectName
	 * @throws IOException
	 */
	public static void fileUpload(MultipartFile file, String fileLocation)
			throws IOException {
		// Prelims.
		byte[] bytes = file.getBytes();
		checkDirectoryExistence(fileLocation);

		// Upload file.
		FileOutputStream oStream = new FileOutputStream(new File(fileLocation));
		BufferedOutputStream stream = new BufferedOutputStream(oStream);
		stream.write(bytes);
		stream.close();
		oStream.close();
	}

	/**
	 * Helper function to create non-existing folders.
	 * 
	 * @param fileLocation
	 */
	public static void checkDirectoryExistence(String fileLocation) {
		File file = new File(fileLocation);
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
	}

	public static void deletePhysicalFile(String location) {
		File phyFile = new File(location);
		phyFile.delete();
	}

	public static String constructSysHomeFileURI(String sysHome,
			long companyID, String className, long objID, String fileName) {
		String fileLocation = sysHome;
		fileLocation += "/company/" + companyID;
		fileLocation += "/" + className.toLowerCase() + "/" + objID;
		fileLocation += "/files/" + fileName;
		return fileLocation;
	}

}
