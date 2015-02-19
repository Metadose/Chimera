package com.cebedo.pmsys.projectfile.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.projectfile.model.ProjectFile;
import com.cebedo.pmsys.projectfile.service.ProjectFileService;
import com.cebedo.pmsys.staff.model.Staff;

@Controller
@RequestMapping(ProjectFile.OBJECT_NAME)
public class ProjectFileController {

	public static final String ATTR_LIST = "projectFileList";
	public static final String ATTR_PROJECTFILE = ProjectFile.OBJECT_NAME;
	public static final String JSP_LIST = "projectFileList";
	public static final String JSP_EDIT = "projectFileEdit";

	private ProjectFileService projectFileService;

	@Autowired(required = true)
	@Qualifier(value = "projectFileService")
	public void setProjectFileService(ProjectFileService ps) {
		this.projectFileService = ps;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listProjectFiles(Model model) {
		model.addAttribute(ATTR_LIST,
				this.projectFileService.listWithAllCollections());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(
			@ModelAttribute(ATTR_PROJECTFILE) ProjectFile projectFile) {
		if (projectFile.getId() == 0) {
			this.projectFileService.create(projectFile);
		} else {
			this.projectFileService.update(projectFile);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECTFILE + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ ProjectFile.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(ProjectFile.COLUMN_PRIMARY_KEY) int id) {
		this.projectFileService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECTFILE + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ ProjectFile.COLUMN_PRIMARY_KEY + "}")
	public String editProjectFile(
			@PathVariable(ProjectFile.COLUMN_PRIMARY_KEY) int id, Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_PROJECTFILE, new ProjectFile());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_PROJECTFILE,
				this.projectFileService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

	private void fileUpload(MultipartFile file) throws IOException {
		byte[] bytes = file.getBytes();
		// TODO Make SYS_HOME directory.
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(new File("C:/temp/"
						+ file.getOriginalFilename())));
		stream.write(bytes);
		stream.close();
	}

	@RequestMapping(value = SystemConstants.REQUEST_UPLOAD_FILE, method = RequestMethod.POST)
	public ModelAndView handleFileUpload(
			@RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description)
			throws IOException {

		// If file is not empty.
		if (!file.isEmpty()) {

			// Upload the file to the server.
			fileUpload(file);

			// Fetch some details and set.
			long size = file.getSize();
			Date dateUploaded = new Date(System.currentTimeMillis());
			ProjectFile projectFile = new ProjectFile();

			// TODO Location and Uploader.
			projectFile.setLocation("C:/");
			projectFile.setUploader(new Staff(1));

			projectFile.setName(file.getOriginalFilename());
			projectFile.setDescription(description);
			projectFile.setSize(size);
			projectFile.setDateUploaded(dateUploaded);
			this.projectFileService.create(projectFile);
		} else {
			// TODO Handle this scenario.
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ ProjectFile.OBJECT_NAME + "/" + SystemConstants.REQUEST_LIST);
	}

	/**
	 * Update a description of a file.
	 * 
	 * @param fileID
	 * @param description
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_UPDATE, method = RequestMethod.POST)
	public ModelAndView updateDescription(
			@RequestParam(ProjectFile.COLUMN_PRIMARY_KEY) long fileID,
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description) {
		this.projectFileService.updateDescription(fileID, description);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ ProjectFile.OBJECT_NAME + "/" + SystemConstants.REQUEST_LIST);
	}
}