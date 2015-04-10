package com.cebedo.pmsys.project.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.company.dao.CompanyDAO;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.security.audit.dao.AuditLogDAO;
import com.cebedo.pmsys.security.audit.model.AuditLog;
import com.cebedo.pmsys.system.helper.AuthHelper;
import com.cebedo.pmsys.system.helper.BeanHelper;
import com.cebedo.pmsys.system.helper.LogHelper;
import com.cebedo.pmsys.system.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.system.message.receiver.MessageReceiver;
import com.cebedo.pmsys.system.message.sender.MessageSender;

@Service
public class ProjectServiceImpl implements ProjectService {

	private static Logger logger = Logger.getLogger(Project.OBJECT_NAME);
	private AuthHelper authHelper = new AuthHelper();
	private LogHelper logHelper = new LogHelper();
	private BeanHelper beanHelper = new BeanHelper();

	private ProjectDAO projectDAO;
	private CompanyDAO companyDAO;
	private AuditLogDAO auditLogDAO;

	public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
		this.auditLogDAO = auditLogDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	@Override
	@Transactional
	public void create(Project project) {
		AuthenticationToken auth = this.authHelper.getAuth();
		String logText = this.logHelper.generateLogMessage(auth,
				"Creating project: " + project.getName());
		logger.info(logText);

		this.projectDAO.create(project);
		Company authCompany = auth.getCompany();
		if (this.authHelper.notNullObjNotSuperAdmin(authCompany)) {
			project.setCompany(authCompany);
			this.projectDAO.update(project);
		}

		// Audit the action.
		AuditLog audit = new AuditLog(AuditLog.ACTION_CREATE);
		audit.setCompany(auth.getCompany() == null ? project.getCompany()
				: auth.getCompany());
		audit.setObjectName(Project.OBJECT_NAME);
		audit.setObjectID(project.getId());
		this.auditLogDAO.create(audit);

		// TODO Send messages/notifications.
		// OR use message brokers as instructions.
		// LIKE send message to logger to log.
		// AND auditor to audit.
		MessageSender sender = (MessageSender) this.beanHelper
				.getBean(MessageSender.BEAN_NAME);
		sender.send("notification.user-[userid].project.create", logText);

		MessageReceiver receiver = (MessageReceiver) this.beanHelper
				.getBean(MessageReceiver.BEAN_NAME);
		receiver.receive("notification.user-[userid].project.create");
	}

	@Override
	@Transactional
	public void update(Project project) {
		Company company = this.companyDAO.getCompanyByObjID(Project.TABLE_NAME,
				Project.COLUMN_PRIMARY_KEY, project.getId());
		project.setCompany(company);
		AuthenticationToken auth = this.authHelper.getAuth();

		if (this.authHelper.isActionAuthorized(project)) {
			logger.info(this.logHelper.generateLogMessage(
					auth,
					"Updating project: " + project.getId() + " = "
							+ project.getName()));
			this.projectDAO.update(project);
		} else {
			logger.warn(this.logHelper.generateLogMessage(auth,
					"Not authorized to update project: " + project.getId()
							+ " = " + project.getName()));
		}

		// Audit the action.
		AuditLog audit = new AuditLog(AuditLog.ACTION_UPDATE);
		audit.setCompany(auth.getCompany() == null ? company : auth
				.getCompany());
		audit.setObjectName(Project.OBJECT_NAME);
		audit.setObjectID(project.getId());
		this.auditLogDAO.create(audit);
	}

	@Override
	@Transactional
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
	public void delete(long id) {
		AuthenticationToken auth = this.authHelper.getAuth();
		Project project = this.projectDAO.getByID(id);

		if (this.authHelper.isActionAuthorized(project)) {
			logger.info(this.logHelper.generateLogMessage(auth,
					"Deleting project: " + id + " = " + project.getName()));
			this.projectDAO.delete(id);
		} else {
			logger.warn(this.logHelper.generateLogMessage(
					auth,
					"Not authorized to delete project: " + id + " = "
							+ project.getName()));
		}

		// Audit the action.
		AuditLog audit = new AuditLog(AuditLog.ACTION_DELETE);
		audit.setCompany(auth.getCompany() == null ? project.getCompany()
				: auth.getCompany());
		audit.setObjectName(Project.OBJECT_NAME);
		audit.setObjectID(project.getId());
		this.auditLogDAO.create(audit);
	}

	@Override
	@Transactional
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
	public Project getByIDWithAllCollections(long id) {
		AuthenticationToken auth = this.authHelper.getAuth();
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
	public String getNameByID(long projectID) {
		AuthenticationToken token = this.authHelper.getAuth();
		String name = this.projectDAO.getNameByID(projectID);
		logger.info(this.logHelper.generateLogMessage(token,
				"Getting name of project: " + projectID + " = " + name));
		return name;
	}
}