package com.cebedo.pmsys.company.dao;

import java.util.List;

import com.cebedo.pmsys.company.model.Company;

public interface CompanyDAO {

	public void create(Company company);

	public Company getByID(long id);

	public void update(Company company);

	public void delete(long id);

	public List<Company> list();

}
