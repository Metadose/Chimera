package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.MaterialToRemove;
import com.cebedo.pmsys.model.Project;

public interface MaterialDAO {

    public void create(MaterialToRemove material);

    public MaterialToRemove getByID(long id);

    public void update(MaterialToRemove material);

    public void delete(long id);

    public List<MaterialToRemove> list();

    public List<MaterialToRemove> list(Project project);

}