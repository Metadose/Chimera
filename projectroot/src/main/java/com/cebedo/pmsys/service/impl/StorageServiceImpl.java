package com.cebedo.pmsys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.StorageDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Storage;
import com.cebedo.pmsys.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {

    private AuthHelper authHelper = new AuthHelper();
    private StorageDAO storageDAO;

    public void setStorageDAO(StorageDAO storageDAO) {
	this.storageDAO = storageDAO;
    }

    @Override
    @Transactional
    public void create(Storage storage) {
	if (this.authHelper.isActionAuthorized(storage)) {
	    this.storageDAO.create(storage);
	}
    }

    @Override
    @Transactional
    public Storage getByID(long id) {
	Storage storage = this.storageDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(storage)) {
	    return storage;
	}
	return new Storage();
    }

    @Override
    @Transactional
    public void update(Storage storage) {
	if (this.authHelper.isActionAuthorized(storage)) {
	    this.storageDAO.update(storage);
	}
    }

    @Override
    @Transactional
    public void delete(long id) {
	Storage storage = this.storageDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(storage)) {
	    this.storageDAO.delete(id);
	}
    }

    @Override
    @Transactional
    public List<Storage> list() {
	return this.storageDAO.list();
    }

}
