package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Formula;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.repository.FormulaValueRepo;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;

@Service
public class FormulaServiceImpl implements FormulaService {

    private AuthHelper authHelper = new AuthHelper();
    private FormulaValueRepo formulaValueRepo;

    public void setFormulaValueRepo(FormulaValueRepo formulaValueRepo) {
	this.formulaValueRepo = formulaValueRepo;
    }

    @Override
    @Transactional
    public void rename(Formula obj, String newKey) {
	this.formulaValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, Formula> m) {
	this.formulaValueRepo.multiSet(m);
    }

    @Override
    @Transactional
    public String set(Formula obj) {

	if (obj.getCompany() == null) {
	    obj.setCompany(this.authHelper.getAuth().getCompany());
	}

	// If we're creating.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.formulaValueRepo.set(obj);
	    return AlertBoxFactory.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_FORMULA, obj.getName());
	}
	this.formulaValueRepo.set(obj);
	return AlertBoxFactory.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_FORMULA, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.formulaValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(Formula obj) {
	this.formulaValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public Formula get(String uuid) {
	String key = Formula.constructKey(this.authHelper.getAuth()
		.getCompany(), uuid);
	return this.formulaValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.formulaValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<Formula> multiGet(Collection<String> keys) {
	return this.formulaValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public List<Formula> list() {
	AuthenticationToken auth = this.authHelper.getAuth();
	String pattern = Formula.constructPattern(auth.getCompany());
	Set<String> keys = this.formulaValueRepo.keys(pattern);
	List<Formula> fList = this.formulaValueRepo.multiGet(keys);
	return fList;
    }
}
