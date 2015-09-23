package com.cebedo.pmsys.service;

import java.util.Date;

import com.cebedo.pmsys.bean.PayrollResultComputation;
import com.cebedo.pmsys.domain.ProjectPayroll;

public interface ProjectPayrollComputerService {

    public PayrollResultComputation getPayrollResult();

    public void compute(Date min, Date max, ProjectPayroll projectPayroll);

    public String getPayrollJSONResult();

}
