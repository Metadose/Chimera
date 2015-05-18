package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.Company;

public interface CompanyService {

	public String create(Company company);

	public Company getByID(long id);

	public String update(Company company);

	public String delete(long id);

	public List<Company> list();

}
