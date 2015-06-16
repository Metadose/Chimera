package com.cebedo.pmsys.service;

import java.util.Date;

import com.cebedo.pmsys.bean.PayrollComputationResult;
import com.cebedo.pmsys.domain.ProjectPayroll;

public interface ProjectPayrollComputerService {

    public PayrollComputationResult getPayrollResult();

    public void compute(Date min, Date max, ProjectPayroll projectPayroll);

    public String getPayrollJSONResult();

}
