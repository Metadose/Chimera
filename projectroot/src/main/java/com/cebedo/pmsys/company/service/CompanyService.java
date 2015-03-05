package com.cebedo.pmsys.company.service;

import java.util.List;

import com.cebedo.pmsys.company.model.Company;

public interface CompanyService {

	public void create(Company company);

	public Company getByID(long id);

	public void update(Company company);

	public void delete(long id);

	public List<Company> list();

}
