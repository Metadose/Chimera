package com.cebedo.pmsys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;

@Service
public class CompanyServiceImpl implements CompanyService {

	private AuthHelper authHelper = new AuthHelper();
	private CompanyDAO companyDAO;

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	@Override
	@Transactional
	public String create(Company company) {
		AuthenticationToken auth = this.authHelper.getAuth();

		String result = "";

		if (auth.isSuperAdmin()) {
			this.companyDAO.create(company);
			result = AlertBoxFactory.SUCCESS.generateCreate(
					Company.OBJECT_NAME, company.getName());
		} else {
			result = AlertBoxFactory.FAILED.generateCreate(Company.OBJECT_NAME,
					company.getName());
		}
		return result;
	}

	@Override
	@Transactional
	public Company getByID(long id) {
		return this.companyDAO.getByID(id);
	}

	@Override
	@Transactional
	public String update(Company company) {
		AuthenticationToken auth = this.authHelper.getAuth();

		String result = "";

		if (auth.isSuperAdmin()) {
			this.companyDAO.update(company);
			result = AlertBoxFactory.SUCCESS.generateUpdate(
					Company.OBJECT_NAME, company.getName());
		} else {
			result = AlertBoxFactory.FAILED.generateUpdate(Company.OBJECT_NAME,
					company.getName());
		}
		return result;
	}

	@Override
	@Transactional
	public String delete(long id) {
		String result = "";
		AuthenticationToken auth = this.authHelper.getAuth();
		Company company = this.companyDAO.getByID(id);
		if (auth.isSuperAdmin()) {
			this.companyDAO.delete(id);
			result = AlertBoxFactory.SUCCESS.generateDelete(
					Company.OBJECT_NAME, company.getName());
		} else {
			result = AlertBoxFactory.FAILED.generateDelete(Company.OBJECT_NAME,
					company.getName());
		}
		return result;
	}

	@Override
	@Transactional
	public List<Company> list() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.companyDAO.list(null);
		}
		return new ArrayList<Company>();
	}
}
