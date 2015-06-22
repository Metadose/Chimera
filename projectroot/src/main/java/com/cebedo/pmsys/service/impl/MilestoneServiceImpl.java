package com.cebedo.pmsys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.MilestoneDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.service.MilestoneService;

@Service
public class MilestoneServiceImpl implements MilestoneService {

    private AuthHelper authHelper = new AuthHelper();
    private MilestoneDAO milestoneDAO;

    public void setMilestoneDAO(MilestoneDAO milestoneDAO) {
	this.milestoneDAO = milestoneDAO;
    }

    @Override
    @Transactional
    public void create(Milestone milestone) {
	if (this.authHelper.isActionAuthorized(milestone)) {
	    this.milestoneDAO.create(milestone);
	}
    }

    @Override
    @Transactional
    public Milestone getByID(long id) {
	Milestone milestone = this.milestoneDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(milestone)) {
	    return milestone;
	}
	return new Milestone();
    }

    @Override
    @Transactional
    public void update(Milestone milestone) {
	if (this.authHelper.isActionAuthorized(milestone)) {
	    this.milestoneDAO.update(milestone);
	}
    }

    @Override
    @Transactional
    public void delete(long id) {
	Milestone milestone = this.milestoneDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(milestone)) {
	    this.milestoneDAO.delete(id);
	}
    }

    @Override
    @Transactional
    public List<Milestone> list() {
	return this.milestoneDAO.list();
    }

}
