package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.SupplierDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Supplier;

@Service
public class SupplierServiceImpl implements SupplierService {

    private AuthHelper authHelper = new AuthHelper();
    private SupplierDAO supplierDAO;

    public void setSupplierDAO(SupplierDAO supplierDAO) {
	this.supplierDAO = supplierDAO;
    }

    @Override
    @Transactional
    public void create(Supplier supplier) {
	if (this.authHelper.isActionAuthorized(supplier)) {
	    this.supplierDAO.create(supplier);
	}
    }

    @Override
    @Transactional
    public Supplier getByID(long id) {
	Supplier supplier = this.supplierDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(supplier)) {
	    return supplier;
	}
	return new Supplier();
    }

    @Override
    @Transactional
    public void update(Supplier supplier) {
	if (this.authHelper.isActionAuthorized(supplier)) {
	    this.supplierDAO.update(supplier);
	}
    }

    @Override
    @Transactional
    public void delete(long id) {
	Supplier supplier = this.supplierDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(supplier)) {
	    this.supplierDAO.delete(id);
	}
    }

    @Override
    @Transactional
    public List<Supplier> list() {
	return this.supplierDAO.list();
    }

}
