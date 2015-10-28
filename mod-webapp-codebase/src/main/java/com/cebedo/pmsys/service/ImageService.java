package com.cebedo.pmsys.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cebedo.pmsys.constants.RegistryURL;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;

/**
 * Keep this class just for reference.
 */
@Controller
@RequestMapping(ImageService.SERVICE_NAME)
public class ImageService {

    private AuthHelper authHelper = new AuthHelper();
    public static final String SERVICE_NAME = "image";
    public static final String PARAM_FILENAME = "filename";

    @Value("${webapp.config.files.home}")
    private String serverHome;

    @RequestMapping(RegistryURL.DISPLAY_COMPANY_LOGO)
    public ResponseEntity<byte[]> displayCompanyLogo() throws IOException {

	Company company = this.authHelper.getAuth().getCompany();
	String companyLogoPath = String.format("%s/company/%s/logo/logo.png", this.serverHome,
		company.getId());

	// Convert to bytes.
	File file = new File(companyLogoPath);
	if (!file.exists()) {
	    return null;
	}
	InputStream imgStream = new FileInputStream(companyLogoPath);
	byte[] imgBytes = IOUtils.toByteArray(imgStream);
	imgStream.close();

	// Send it back to user.
	final HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.IMAGE_PNG);
	return new ResponseEntity<byte[]>(imgBytes, headers, HttpStatus.CREATED);
    }
}
