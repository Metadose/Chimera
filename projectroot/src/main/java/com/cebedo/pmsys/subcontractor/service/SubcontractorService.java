package com.cebedo.pmsys.subcontractor.service;

import java.util.List;

import com.cebedo.pmsys.subcontractor.model.Subcontractor;

public interface SubcontractorService {

	public void create(Subcontractor subcontractor);

	public Subcontractor getByID(long id);

	public void update(Subcontractor subcontractor);

	public void delete(long id);

	public List<Subcontractor> list();

	public List<Subcontractor> listWithAllCollections();
}
