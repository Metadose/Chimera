package com.cebedo.pmsys.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.model.Company;

public class FileHelper {

    @SuppressWarnings("resource")
    public String getFileContents(String path) throws FileNotFoundException {
	File file = new File(path);
	if (file.length() == 0) {
	    return "";
	}
	return new Scanner(new File(path)).useDelimiter("\\Z").next();
    }

    /**
     * Upload a file.
     * 
     * @param file
     * @param id
     * @param objectName
     * @throws IOException
     */
    public void fileUpload(MultipartFile file, String fileLocation) throws IOException {
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

    public void deletePhysicalFile(String location) {
	File phyFile = new File(location);
	phyFile.delete();
    }

    public String constructSysHomeFileURI(String sysHome, long companyID, String className, long objID,
	    String moduleName, String fileName) {
	String companyClass = Company.class.getSimpleName().toLowerCase();

	String fileLocation = sysHome;
	fileLocation += "/" + companyClass + "/" + companyID;
	fileLocation += "/" + className.toLowerCase() + "/" + objID;
	fileLocation += "/" + moduleName.toLowerCase() + "/" + fileName;
	return fileLocation;
    }

}
