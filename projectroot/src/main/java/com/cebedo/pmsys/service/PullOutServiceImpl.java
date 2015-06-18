package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.repository.PullOutValueRepo;

@Service
public class PullOutServiceImpl implements PullOutService {

    private PullOutValueRepo pullOutValueRepo;

    public void setPullOutValueRepo(PullOutValueRepo pullOutValueRepo) {
	this.pullOutValueRepo = pullOutValueRepo;
    }

    @Override
    @Transactional
    public void rename(PullOut obj, String newKey) {
	this.pullOutValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, PullOut> m) {
	this.pullOutValueRepo.multiSet(m);
    }

    @Override
    @Transactional
    public void set(PullOut obj) {
	this.pullOutValueRepo.set(obj);
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.pullOutValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(PullOut obj) {
	this.pullOutValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public PullOut get(String key) {
	return this.pullOutValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.pullOutValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<PullOut> multiGet(Collection<String> keys) {
	return this.pullOutValueRepo.multiGet(keys);
    }

    @Transactional
    @Override
    public void delete(String key) {
	this.pullOutValueRepo.delete(key);
    }

}
