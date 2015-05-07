package com.cebedo.pmsys.subcontractor.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.subcontractor.dao.SubcontractorDAO;
import com.cebedo.pmsys.subcontractor.model.Subcontractor;
import com.cebedo.pmsys.system.helper.AuthHelper;
import com.cebedo.pmsys.system.login.authentication.AuthenticationToken;

@Service
public class SubcontractorServiceImpl implements SubcontractorService {

	private AuthHelper authHelper = new AuthHelper();
	private SubcontractorDAO subcontractorDAO;

	public void setSubcontractorDAO(SubcontractorDAO subcontractorDAO) {
		this.subcontractorDAO = subcontractorDAO;
	}

	@Override
	@Transactional
	public void create(Subcontractor subcontractor) {
		AuthenticationToken auth = this.authHelper.getAuth();
		Company authCompany = auth.getCompany();
		subcontractor.setCompany(authCompany);
		this.subcontractorDAO.create(subcontractor);
	}

	@Override
	@Transactional
	public Subcontractor getByID(long id) {
		Subcontractor subcon = this.subcontractorDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(subcon)) {
			return subcon;
		}
		return new Subcontractor();
	}

	@Override
	@Transactional
	public void update(Subcontractor subcontractor) {
		if (this.authHelper.isActionAuthorized(subcontractor)) {
			this.subcontractorDAO.update(subcontractor);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		Subcontractor subcon = this.subcontractorDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(subcon)) {
			this.subcontractorDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Subcontractor> list() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.subcontractorDAO.list(null);
		}
		return this.subcontractorDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public List<Subcontractor> listWithAllCollections() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.subcontractorDAO.listWithAllCollections(null);
		}
		return this.subcontractorDAO.listWithAllCollections(token.getCompany()
				.getId());
	}
}
