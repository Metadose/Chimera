package com.cebedo.pmsys.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

    private SessionFactory sessionFactory;
    private DAOHelper daoHelper = new DAOHelper();

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    public void create(Project project) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(project);
    }

    @SuppressWarnings("unchecked")
    public List<Project> list(Long companyID) {
	Session session = this.sessionFactory.getCurrentSession();
	List<Project> projectList = this.daoHelper
		.getSelectQueryFilterCompany(session, Project.class.getName(), companyID).list();
	return projectList;
    }

    @Override
    public Project getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Project project = (Project) session.load(Project.class.getName(), new Long(id));
	return project;
    }

    @Override
    public void update(Project project) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(project);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Project project = getByID(id);
	if (project != null) {
	    session.delete(project);
	}
    }

    @SuppressWarnings("unchecked")
    public List<Project> listWithAllCollections(Long companyID) {
	Session session = this.sessionFactory.getCurrentSession();
	List<Project> projectList = this.daoHelper
		.getSelectQueryFilterCompany(session, Project.class.getName(), companyID).list();
	for (Project project : projectList) {
	    Hibernate.initialize(project.getAssignedTasks());
	}
	return projectList;
    }

    @Override
    public Project getByIDWithAllCollections(long id) {

	Session session = this.sessionFactory.getCurrentSession();
	Project project = (Project) session.load(Project.class, new Long(id));

	// Init company.
	// and company admins.
	Hibernate.initialize(project.getCompany());
	Company co = project.getCompany();
	if (co != null) {
	    for (Staff admin : co.getAdmins()) {
		Hibernate.initialize(admin);
	    }
	}

	Hibernate.initialize(project.getAssignedFields());
	Hibernate.initialize(project.getAssignedStaff());

	// Initialize all tasks.
	// And all teams and staff of each task.
	Set<Task> assignedTasks = project.getAssignedTasks();
	Hibernate.initialize(assignedTasks);
	for (Task task : assignedTasks) {
	    Hibernate.initialize(task.getStaff());
	}

	return project;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Project> listWithTasks(Long id) {
	Session session = this.sessionFactory.getCurrentSession();
	List<Project> projectList = this.daoHelper
		.getSelectQueryFilterCompany(session, Project.class.getName(), id).list();
	for (Project project : projectList) {
	    Hibernate.initialize(project.getAssignedTasks());
	}
	return projectList;
    }

    @Override
    public String getNameByID(long projectID) {
	Session session = this.sessionFactory.getCurrentSession();
	String result = this.daoHelper.getProjectionByID(session, Project.class, Project.PROPERTY_ID,
		projectID, Project.PROPERTY_NAME);
	return result;
    }

    @Override
    public void merge(Project project) {
	Session session = this.sessionFactory.getCurrentSession();
	session.merge(project);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AuditLog> logs(long projID, Company company) {
	Session session = this.sessionFactory.getCurrentSession();
	Criteria criteria = session.createCriteria(AuditLog.class);
	criteria.add(Restrictions.eq(Company.OBJECT_NAME, company));
	criteria.add(Restrictions.eq("objectName", Project.OBJECT_NAME));
	criteria.add(Restrictions.eq("objectID", projID));

	SimpleExpression ltAction = Restrictions.lt("action", AuditAction.ACTION_GET.id());
	SimpleExpression gtAction = Restrictions.lt("action", AuditAction.ACTION_RANGE.id());
	criteria.add(Restrictions.and(ltAction, gtAction));
	return criteria.list();
    }
}