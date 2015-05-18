package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Subcontractor;

public interface SubcontractorDAO {

	public void create(Subcontractor subcontractor);

	public Subcontractor getByID(long id);

	public void update(Subcontractor subcontractor);

	public void delete(long id);

	public List<Subcontractor> list(Long companyID);

	public List<Subcontractor> listWithAllCollections(Long companyID);
}
