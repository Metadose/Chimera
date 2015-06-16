package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.repository.MaterialValueRepo;

@Service
public class MaterialServiceImpl implements MaterialService {

    private MaterialValueRepo materialValueRepo;

    public void setMaterialValueRepo(MaterialValueRepo materialValueRepo) {
	this.materialValueRepo = materialValueRepo;
    }

    @Override
    @Transactional
    public void rename(Material obj, String newKey) {
	this.materialValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, Material> m) {
	this.materialValueRepo.multiSet(m);
    }

    @Override
    @Transactional
    public void set(Material obj) {
	this.materialValueRepo.set(obj);
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.materialValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(Material obj) {
	this.materialValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public Material get(String key) {
	return this.materialValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.materialValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<Material> multiGet(Collection<String> keys) {
	return this.materialValueRepo.multiGet(keys);
    }

}
