package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Storage;

public interface StorageDAO {

    public void create(Storage storage);

    public Storage getByID(long id);

    public void update(Storage storage);

    public void delete(long id);

    public List<Storage> list();

}