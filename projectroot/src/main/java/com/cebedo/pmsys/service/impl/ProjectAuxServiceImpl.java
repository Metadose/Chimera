package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.service.ProjectAuxService;

@Service
public class ProjectAuxServiceImpl implements ProjectAuxService {

    private ProjectAuxValueRepo projectAuxValueRepo;

    public void setProjectAuxValueRepo(ProjectAuxValueRepo projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    @Override
    @Transactional
    public void rename(ProjectAux obj, String newKey) {
	this.projectAuxValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, ProjectAux> m) {
	this.projectAuxValueRepo.multiSet(m);
    }

    @Override
    @Transactional
    public void set(ProjectAux obj) {
	this.projectAuxValueRepo.set(obj);
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.projectAuxValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(ProjectAux obj) {
	this.projectAuxValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public ProjectAux get(String key) {
	return this.projectAuxValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.projectAuxValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<ProjectAux> multiGet(Collection<String> keys) {
	return this.projectAuxValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public void delete(String key) {
	this.projectAuxValueRepo.delete(key);
    }

    @Override
    @Transactional
    public ProjectAux get(Delivery delivery) {
	String key = ProjectAux.constructKey(delivery.getProject());
	return this.get(key);
    }

    @Override
    @Transactional
    public ProjectAux get(Project proj) {
	String key = ProjectAux.constructKey(proj);
	return this.get(key);
    }

}
