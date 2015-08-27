package com.cebedo.pmsys.helper;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.EstimateComputationInputBean;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.pojo.FormMassAttendance;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

public class ValidationHelper {

    /**
     * Attendance.
     * 
     * @param attendance
     * @return
     */
    public String validate(Attendance attendance) {
	double wage = attendance.getWage(); // < Zero
	if (wage < 0) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_STAFF_ATTENDANCE_NEGATIVE_WAGE);
	}
	return null;
    }

    /**
     * Mass attendance.
     * 
     * @param attendanceMass
     * @return
     */
    public String validate(FormMassAttendance attendanceMass) {
	Date startDate = attendanceMass.getStartDate(); // Start date is after
	Date endDate = attendanceMass.getEndDate(); // End date
	double wage = attendanceMass.getWage(); // < Zero

	// If start date is > end date, error.
	if (startDate.after(endDate)) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_START_DATE_GT_END_DATE);
	} else if (wage < 0) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_STAFF_ATTENDANCE_NEGATIVE_WAGE);
	}
	return null;
    }

    /**
     * Company.
     * 
     * @param company
     * @return
     */
    public String validate(Company company) {
	Date now = new Date(System.currentTimeMillis());
	Date expire = company.getDateExpiration();
	Date start = company.getDateStarted();

	// Expiration is before now.
	if (expire.before(now)) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMPANY_EXPIRE_DATE_LT_NOW);
	}
	// Expiration is before start.
	else if (expire.before(start)) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_START_DATE_GT_END_DATE);
	}
	return null;
    }

    /**
     * Estimation.
     * 
     * @param estimateInput
     * @return
     */
    public String validate(EstimateComputationInputBean estimateInput) {
	MultipartFile file = estimateInput.getEstimationFile();
	// If file is null
	// Or file is empty.
	if (file == null || file.isEmpty()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_ESTIMATION_EMPTY_FILE);
	}
	// TODO Handle case when other file types are uploaded.
	// Filter only Excel files.
	// TODO Test if code works in *.xlsx
	return null;
    }

}
