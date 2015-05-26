package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Company;

public interface CompanyDAO {

	public void create(Company company);

	public Company getByID(long id);

	public void update(Company company);

	public void delete(long id);

	public List<Company> list(Long companyID);

	/**
	 * Use session attributes rather than using this function.
	 * 
	 * @param objTable
	 * @param objKeyCol
	 * @param objID
	 * @return
	 */
	@Deprecated
	public long getCompanyIDByObjID(String objTable, String objKeyCol,
			long objID);

	/**
	 * Use session attributes rather than using this function.
	 * 
	 * @param objTable
	 * @param objKeyCol
	 * @param objID
	 * @return
	 */
	@Deprecated
	public Company getCompanyByObjID(String objTable, String objKeyCol,
			long objID);

}