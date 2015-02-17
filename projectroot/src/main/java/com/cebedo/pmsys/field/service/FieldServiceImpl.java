package com.cebedo.pmsys.field.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.field.dao.FieldDAO;
import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.model.FieldAssignment;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;

@Service
public class FieldServiceImpl implements FieldService {

	private FieldDAO fieldDAO;
	private ProjectDAO projectDAO;

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setFieldDAO(FieldDAO fieldDAO) {
		this.fieldDAO = fieldDAO;
	}

	@Override
	@Transactional
	public void create(Field field) {
		this.fieldDAO.create(field);
	}

	@Override
	@Transactional
	public Field getByID(long id) {
		return this.fieldDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(Field field) {
		this.fieldDAO.update(field);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.fieldDAO.delete(id);
	}

	@Override
	@Transactional
	public List<Field> list() {
		return this.fieldDAO.list();
	}

	@Override
	@Transactional
	public List<Field> listWithAllCollections() {
		return this.fieldDAO.listWithAllCollections();
	}

	@Override
	@Transactional
	public void assign(FieldAssignment fieldAssignment, long fieldID,
			long projectID) {
		Field field = this.fieldDAO.getByID(fieldID);
		Project proj = this.projectDAO.getByID(projectID);
		fieldAssignment.setField(field);
		fieldAssignment.setProject(proj);
		this.fieldDAO.assign(fieldAssignment);
	}

}
