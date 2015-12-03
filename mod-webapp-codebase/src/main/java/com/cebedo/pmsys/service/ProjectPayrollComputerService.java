package com.cebedo.pmsys.service;

import java.util.Date;
import java.util.Map;

import com.cebedo.pmsys.bean.PairCountValue;
import com.cebedo.pmsys.bean.PayrollResultComputation;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.StatusAttendance;
import com.cebedo.pmsys.model.Staff;

public interface ProjectPayrollComputerService {

    public String getPayrollJSONResult(Map<Staff, Double> staffToWageMap,
	    Map<Staff, Map<StatusAttendance, PairCountValue>> staffPayrollBreakdownMap);

    public PayrollResultComputation getPayrollResult();

    public void compute(Date min, Date max, ProjectPayroll projectPayroll);

    public String getPayrollJSONResult();

}
