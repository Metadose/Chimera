package com.cebedo.pmsys.service;

import java.util.Date;

import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.model.Project;

public interface ProjectPayrollComputerService {

    public void compute(Project proj, Date min, Date max,
	    ProjectPayroll projectPayroll);

    public String getPayrollJSONResult();

}
