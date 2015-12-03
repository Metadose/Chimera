package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.pojo.FormMultipartFile;

public interface CompanyService {

    @PreAuthorize("hasRole('ADMIN_SUPER')")
    public String clone(long companyId, String cloneName);

    @PreAuthorize("hasRole('ADMIN_SUPER')")
    public String create(Company company, BindingResult result);

    public Company getByID(long id);

    public String update(Company company, BindingResult result);

    @PreAuthorize("hasRole('ADMIN_SUPER')")
    public String delete(long id);

    public List<Company> list();

    public List<AuditLog> logs();

    public Company settings();

    public String uploadCompanyLogo(Company company, FormMultipartFile companyLogo,
	    BindingResult result);

}
