package com.cebedo.pmsys.photo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.photo.model.Photo;
import com.cebedo.pmsys.photo.service.PhotoService;

@Controller
@RequestMapping(Photo.OBJECT_NAME)
public class PhotoController {

	public static final String ATTR_LIST = "photoList";
	public static final String ATTR_PHOTO = Photo.OBJECT_NAME;
	public static final String JSP_LIST = "photoList";
	public static final String JSP_EDIT = "photoEdit";

	private PhotoService photoService;

	@Autowired(required = true)
	@Qualifier(value = "photoService")
	public void setPhotoService(PhotoService ps) {
		this.photoService = ps;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listPhotos(Model model) {
		model.addAttribute(ATTR_LIST, this.photoService.list());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_PHOTO) Photo photo) {
		if (photo.getId() == 0) {
			this.photoService.create(photo);
		} else {
			this.photoService.update(photo);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PHOTO + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ Photo.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Photo.COLUMN_PRIMARY_KEY) int id) {
		this.photoService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PHOTO + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ Photo.COLUMN_PRIMARY_KEY + "}")
	public String editPhoto(@PathVariable(Photo.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_PHOTO, new Photo());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_PHOTO, this.photoService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}
}