package com.cebedo.pmsys.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Keep this class just for reference.
 */
@Deprecated
@Controller
@RequestMapping(ImageService.SERVICE_NAME)
public class ImageService {

    public static final String SERVICE_NAME = "image";
    public static final String PARAM_FILENAME = "filename";

    // @RequestMapping(SystemConstants.REQUEST_DISPLAY)
    // public ResponseEntity<byte[]>
    // display(@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
    // @RequestParam(PARAM_FILENAME) String fileName) throws IOException {
    // // If not authorized to view this image, return.
    // // TODO Handle this, don't let it crash.
    // Project proj = this.projectService.getByID(projectID);
    // if (!this.authHelper.isActionAuthorized(proj)) {
    // return null;
    // }
    //
    // // Otherwise, user proj company, over user company.
    // // If both are not existing, set to zero.
    // String sysHome =
    // this.configService.getValueByName(SystemConstants.CONFIG_SYS_HOME);
    // Company projCompany = proj.getCompany();
    // Company userCompany = this.authHelper.getAuth().getCompany();
    // String fileURI = this.fileHelper.constructSysHomeFileURI(
    // sysHome,
    // projCompany == null ? userCompany == null ? 0 : userCompany.getId() :
    // projCompany
    // .getId(), Project.class.getSimpleName(), projectID,
    // Photo.class.getSimpleName(),
    // fileName);
    //
    // // Convert to bytes.
    // InputStream imgStream = new FileInputStream(fileURI);
    // byte[] imgBytes = IOUtils.toByteArray(imgStream);
    // imgStream.close();
    //
    // // Send it back to user.
    // final HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.IMAGE_PNG);
    // return new ResponseEntity<byte[]>(imgBytes, headers, HttpStatus.CREATED);
    // }
}
