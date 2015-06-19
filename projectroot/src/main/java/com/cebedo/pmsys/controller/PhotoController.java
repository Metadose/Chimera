package com.cebedo.pmsys.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.model.Photo;
import com.cebedo.pmsys.model.ProjectFile;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.service.PhotoService;
import com.cebedo.pmsys.service.SystemConfigurationService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Controller
@SessionAttributes(Photo.OBJECT_NAME)
@RequestMapping(Photo.OBJECT_NAME)
public class PhotoController {

    public static final String ATTR_LIST = "photoList";
    public static final String ATTR_PHOTO = Photo.OBJECT_NAME;
    public static final String JSP_LIST = "photoList";
    public static final String JSP_EDIT = "photoEdit";

    private PhotoService photoService;
    private SystemConfigurationService configService;
    private String sysHome;

    public String getSysHome() {
	if (sysHome == null) {
	    sysHome = this.configService
		    .getValueByName(SystemConstants.CONFIG_SYS_HOME);
	}
	return sysHome;
    }

    @Autowired(required = true)
    @Qualifier(value = "photoService")
    public void setPhotoService(PhotoService ps) {
	this.photoService = ps;
    }

    @Autowired(required = true)
    @Qualifier(value = "systemConfigurationService")
    public void setFieldService(SystemConfigurationService ps) {
	this.configService = ps;
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UPLOAD_TO_STAFF_PROFILE, method = RequestMethod.POST)
    public ModelAndView uploadStaffProfile(
	    @RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
	    @RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID)
	    throws IOException {

	// If file is not empty.
	if (!file.isEmpty()) {
	    this.photoService.uploadProfilePicOfStaff(file, staffID);
	} else {
	    // TODO Handle this scenario.
	}
	return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
		+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
		+ staffID);
    }

    @RequestMapping(value = { SystemConstants.REQUEST_ROOT,
	    SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String listPhotos(Model model) {
	model.addAttribute(ATTR_LIST, this.photoService.list());
	return JSP_LIST;
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PHOTO_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_PHOTO) Photo photo,
	    RedirectAttributes redirectAttrs) {
	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;
	if (photo.getId() == 0) {
	    // TODO
	    // this.photoService.create(photo);
	} else {
	    alertFactory.setMessage("Successfully <b>updated</b> the photo <b>"
		    + photo.getName() + "</b>.");
	    this.photoService.update(photo);
	}
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		alertFactory.generateHTML());
	return SystemConstants.CONTROLLER_REDIRECT + ATTR_PHOTO + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/"
	    + SystemConstants.FROM + "/" + SystemConstants.ORIGIN)
    public String editPhotoFromOrigin(
	    @RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
	    @RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID,
	    @PathVariable(Photo.COLUMN_PRIMARY_KEY) int id, Model model) {

	model.addAttribute(SystemConstants.ORIGIN, origin);
	model.addAttribute(SystemConstants.ORIGIN_ID, originID);

	if (id == 0) {
	    model.addAttribute(ATTR_PHOTO, new Photo());
	    return JSP_EDIT;
	}

	model.addAttribute(ATTR_PHOTO, this.photoService.getByID(id));

	return JSP_EDIT;
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PHOTO_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
	    + Photo.COLUMN_PRIMARY_KEY + "}")
    public String editPhoto(@PathVariable(Photo.COLUMN_PRIMARY_KEY) int id,
	    Model model) {
	if (id == 0) {
	    model.addAttribute(ATTR_PHOTO, new Photo());
	    return JSP_EDIT;
	}
	model.addAttribute(ATTR_PHOTO, this.photoService.getByID(id));
	return JSP_EDIT;
    }
}