package com.cebedo.pmsys.service;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.Notification;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.NotificationZSetRepo;
import com.cebedo.pmsys.token.AuthenticationToken;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static Logger logger = Logger.getLogger(Project.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private ProjectDAO projectDAO;
    private CompanyDAO companyDAO;
    private NotificationZSetRepo notificationZSetRepo;

    public void setNotificationZSetRepo(
	    NotificationZSetRepo notificationZSetRepo) {
	this.notificationZSetRepo = notificationZSetRepo;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
    }

    @Override
    @Transactional
    @Caching(evict = {
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":search", key = "#project.getCompany() == null ? 0 : #project.getCompany().getId()") })
    public void create(Project project) {
	// Send messages/notifications.
	// Use message brokers as instructions.
	// LIKE send message to logger to log.
	// AND auditor to audit.
	// Fire up the message so that it would go parallel with the service
	// below.
	AuthenticationToken auth = this.authHelper.getAuth();

	String notifTxt = auth.getStaff() == null ? auth.getUser()
		.getUsername() : auth.getStaff().getFullName()
		+ " created a new project " + project.getName();

	Notification notification = new Notification("Test Content", auth
		.getUser().getId());

	this.notificationZSetRepo.add(notification);
	// this.messageHelper.constructSendMessage(project,
	// AuditLog.ACTION_CREATE,
	// "Creating project: " + project.getName());

	// Do service.
	Company authCompany = auth.getCompany();
	project.setCompany(authCompany);
	this.projectDAO.create(project);
    }

    @Override
    @Transactional
    @Caching(evict = {
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getNameByID", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME
		    + ":getByIDWithAllCollections", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getByID", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":search", key = "#project.getCompany() == null ? 0 : #project.getCompany().getId()") })
    public void update(Project project) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(project)) {
	    // Audit and log.
	    this.messageHelper.constructSendMessage(
		    project,
		    AuditLog.ACTION_UPDATE,
		    "Updating project: " + project.getId() + " = "
			    + project.getName());

	    // Actual service.
	    Company company = this.companyDAO.getCompanyByObjID(
		    Project.TABLE_NAME, Project.COLUMN_PRIMARY_KEY,
		    project.getId());
	    project.setCompany(company);
	    this.projectDAO.update(project);
	} else {
	    logger.warn(this.logHelper.generateLogMessage(auth,
		    "Not authorized to update project: " + project.getId()
			    + " = " + project.getName()));
	}
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":list", unless = "#result.isEmpty()")
    public List<Project> list() {
	AuthenticationToken token = this.authHelper.getAuth();

	if (token.isSuperAdmin()) {
	    logger.info(this.logHelper.generateLogMessage(token,
		    "Listing all projects as super admin."));
	    return this.projectDAO.list(null);
	}
	Company company = token.getCompany();
	logger.info(this.logHelper.generateLogMessage(token,
		"Listing all projects from company: " + company.getId() + " = "
			+ company.getName()));
	return this.projectDAO.list(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getByID", key = "#id")
    public Project getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(project)) {
	    logger.info(this.logHelper.generateLogMessage(auth,
		    "Getting project: " + id + " = " + project.getName()));
	    return project;
	}
	logger.warn(this.logHelper.generateLogMessage(
		auth,
		"Not authorized to get project: " + id + " = "
			+ project.getName()));
	return new Project();
    }

    @Override
    @Transactional
    @Caching(evict = {
	    @CacheEvict(value = Project.OBJECT_NAME + ":getNameByID", key = "#id"),
	    @CacheEvict(value = Project.OBJECT_NAME
		    + ":getByIDWithAllCollections", key = "#id"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getByID", key = "#id"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true) })
    public void delete(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.constructSendMessage(project,
		    AuditLog.ACTION_DELETE, "Deleting project: " + id + " = "
			    + project.getName());
	    this.projectDAO.delete(id);
	} else {
	    logger.warn(this.logHelper.generateLogMessage(
		    auth,
		    "Not authorized to delete project: " + id + " = "
			    + project.getName()));
	}
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":listWithAllCollections")
    public List<Project> listWithAllCollections() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    logger.info(this.logHelper.generateLogMessage(token,
		    "Listing projects with all collections as super admin."));
	    return this.projectDAO.listWithAllCollections(null);
	}
	Company company = token.getCompany();
	logger.info(this.logHelper.generateLogMessage(token,
		"Listing projects with all collections from company: "
			+ company.getId() + " = " + company.getName()));
	return this.projectDAO.listWithAllCollections(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#id")
    public Project getByIDWithAllCollections(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();

	// TODO 86400000 is 24 hours.
	Set<Notification> notifs = this.notificationZSetRepo.rangeByScore(
		Notification.constructKey(auth.getUser().getId(), false),
		System.currentTimeMillis() - 86400000,
		System.currentTimeMillis());
	Project project = this.projectDAO.getByIDWithAllCollections(id);

	if (this.authHelper.isActionAuthorized(project)) {
	    logger.info(this.logHelper.generateLogMessage(auth,
		    "Getting project with all collections: " + project.getId()
			    + " = " + project.getName()));
	    return project;
	}
	logger.warn(this.logHelper.generateLogMessage(auth,
		"Not authorized to get project with all collections: "
			+ project.getId() + " = " + project.getName()));
	return new Project();
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":listWithTasks")
    public List<Project> listWithTasks() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    logger.info(this.logHelper
		    .generateLogMessage(token,
			    "Listing all projects (initiated with tasks) as super admin."));
	    return this.projectDAO.listWithTasks(null);
	}
	Company company = token.getCompany();
	logger.info(this.logHelper.generateLogMessage(token,
		"Listing all projects (initiated with tasks) from company: "
			+ company.getId() + " = " + company.getName()));
	return this.projectDAO.listWithTasks(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getNameByID", key = "#projectID", unless = "#result.isEmpty()")
    public String getNameByID(long projectID) {
	AuthenticationToken token = this.authHelper.getAuth();
	String name = this.projectDAO.getNameByID(projectID);
	logger.info(this.logHelper.generateLogMessage(token,
		"Getting name of project: " + projectID + " = " + name));
	return name;
    }

    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public void clearProjectCache(long projectID) {
	;
    }

    @CacheEvict(value = Project.OBJECT_NAME + ":search", key = "#companyID == null ? 0 : #companyID")
    @Override
    @Transactional
    public void clearSearchCache(Long companyID) {
	;
    }
}