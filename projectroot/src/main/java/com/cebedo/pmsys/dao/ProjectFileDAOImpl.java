package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.ProjectFile;

@Repository
public class ProjectFileDAOImpl implements ProjectFileDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(ProjectFileDAOImpl.class);
	private SessionFactory sessionFactory;
	private DAOHelper daoHelper = new DAOHelper();

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(ProjectFile projectFile) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(projectFile);
		logger.info("[Create] Project File: " + projectFile);
	}

	@Override
	public ProjectFile getByID(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		ProjectFile projectFile = (ProjectFile) session.load(ProjectFile.class,
				new Long(id));
		logger.info("[Get by ID] Project File: " + projectFile);
		return projectFile;
	}

	@Override
	public void update(ProjectFile projectFile) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(projectFile);
		logger.info("[Update] Project File:" + projectFile);
	}

	@Override
	public void delete(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		ProjectFile projectFile = (ProjectFile) session.load(ProjectFile.class,
				new Long(id));
		if (projectFile != null) {
			session.delete(projectFile);
		}
		logger.info("[Delete] Project File: " + projectFile);
	}

	@SuppressWarnings("unchecked")
	public List<ProjectFile> list(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<ProjectFile> projectFileList = this.daoHelper
				.getSelectQueryFilterCompany(session,
						ProjectFile.class.getName(), companyID).list();
		return projectFileList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectFile> listWithAllCollections(Long companyID) {
		Session session = this.sessionFactory.getCurrentSession();
		List<ProjectFile> fileList = this.daoHelper
				.getSelectQueryFilterCompany(session,
						ProjectFile.class.getName(), companyID).list();
		for (ProjectFile file : fileList) {
			Hibernate.initialize(file.getProject());
			Hibernate.initialize(file.getUploader());
		}
		return fileList;
	}

	@Override
	public void updateDescription(long fileID, String description) {
		Session session = this.sessionFactory.getCurrentSession();

		String hql = "UPDATE " + ProjectFile.class.getName()
				+ " SET description = :description"
				+ " WHERE id = :projectfile_id";

		Query query = session.createQuery(hql);
		query.setParameter("description", description);
		query.setParameter("projectfile_id", fileID);
		query.executeUpdate();
	}

	@Override
	public String getNameByID(long fileID) {
		Session session = this.sessionFactory.getCurrentSession();
		String result = this.daoHelper.getProjectionByID(session,
				ProjectFile.class, ProjectFile.PROPERTY_ID, fileID,
				ProjectFile.PROPERTY_NAME);
		return result;
	}

}
