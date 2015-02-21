package com.cebedo.pmsys.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.systemconfiguration.service.SystemConfigurationService;

@Controller
@RequestMapping(ImageService.SERVICE_NAME)
public class ImageService {

	public static final String SERVICE_NAME = "image";
	public static final String PARAM_FILENAME = "filename";

	private SystemConfigurationService configService;
	private ProjectService projectService;

	@Autowired(required = true)
	@Qualifier(value = "systemConfigurationService")
	public void setFieldService(SystemConfigurationService ps) {
		this.configService = ps;
	}

	@Autowired(required = true)
	@Qualifier(value = "projectService")
	public void setProjectService(ProjectService ps) {
		this.projectService = ps;
	}

	@RequestMapping(SystemConstants.REQUEST_DISPLAY_PROJECT_PROFILE)
	public ResponseEntity<byte[]> displayProjectProfile(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID)
			throws IOException {

		Project proj = this.projectService.getByID(projectID);

		InputStream imgStream = new FileInputStream(proj.getThumbnailURL());
		byte[] imgBytes = IOUtils.toByteArray(imgStream);
		imgStream.close();

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(imgBytes, headers, HttpStatus.CREATED);
	}

	@RequestMapping(SystemConstants.REQUEST_DISPLAY)
	public ResponseEntity<byte[]> display(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(PARAM_FILENAME) String fileName) throws IOException {

		String sysHome = this.configService.getValueByName("SYS_HOME");

		InputStream imgStream = new FileInputStream(sysHome
				+ Project.OBJECT_NAME + "/" + projectID + "/photos/" + fileName);
		byte[] imgBytes = IOUtils.toByteArray(imgStream);
		imgStream.close();

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(imgBytes, headers, HttpStatus.CREATED);
	}
}
