package com.cebedo.pmsys.subcontractor.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.subcontractor.dao.SubcontractorDAO;
import com.cebedo.pmsys.subcontractor.model.Subcontractor;

@Service
public class SubcontractorServiceImpl implements SubcontractorService {

	private SubcontractorDAO subcontractorDAO;

	public void setSubcontractorDAO(SubcontractorDAO subcontractorDAO) {
		this.subcontractorDAO = subcontractorDAO;
	}

	@Override
	@Transactional
	public void create(Subcontractor subcontractor) {
		this.subcontractorDAO.create(subcontractor);
		AuthenticationToken auth = AuthUtils.getAuth();
		Company authCompany = auth.getCompany();
		if (AuthUtils.notNullObjNotSuperAdmin(authCompany)) {
			subcontractor.setCompany(authCompany);
			this.subcontractorDAO.update(subcontractor);
		}
	}

	@Override
	@Transactional
	public Subcontractor getByID(long id) {
		Subcontractor subcon = this.subcontractorDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(subcon)) {
			return subcon;
		}
		return new Subcontractor();
	}

	@Override
	@Transactional
	public void update(Subcontractor subcontractor) {
		if (AuthUtils.isActionAuthorized(subcontractor)) {
			this.subcontractorDAO.update(subcontractor);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		Subcontractor subcon = this.subcontractorDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(subcon)) {
			this.subcontractorDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Subcontractor> list() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.subcontractorDAO.list(null);
		}
		return this.subcontractorDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public List<Subcontractor> listWithAllCollections() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.subcontractorDAO.listWithAllCollections(null);
		}
		return this.subcontractorDAO.listWithAllCollections(token.getCompany()
				.getId());
	}
}
