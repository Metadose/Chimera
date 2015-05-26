package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Supplier;

public interface SupplierDAO {

    public void create(Supplier supplier);

    public Supplier getByID(long id);

    public void update(Supplier supplier);

    public void delete(long id);

    public List<Supplier> list();

}