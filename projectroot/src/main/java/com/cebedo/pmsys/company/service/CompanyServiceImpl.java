package com.cebedo.pmsys.company.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.company.dao.CompanyDAO;
import com.cebedo.pmsys.company.model.Company;

@Service
public class CompanyServiceImpl implements CompanyService {

	private CompanyDAO companyDAO;

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	@Override
	@Transactional
	public void create(Company company) {
		this.companyDAO.create(company);
	}

	@Override
	@Transactional
	public Company getByID(long id) {
		return this.companyDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(Company company) {
		this.companyDAO.update(company);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.companyDAO.delete(id);
	}

	@Override
	@Transactional
	public List<Company> list() {
		if (!AuthUtils.getAuth().isSuperAdmin()) {
			return new ArrayList<Company>();
		}
		return this.companyDAO.list();
	}
}
