package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;

public interface CompanyService {

    public String create(Company company, BindingResult result);

    public Company getByID(long id);

    public String update(Company company, BindingResult result);

    public String delete(long id);

    public List<Company> list();

    public List<AuditLog> logs();

}
