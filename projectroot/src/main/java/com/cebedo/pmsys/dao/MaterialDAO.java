package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Material;
import com.cebedo.pmsys.model.Project;

public interface MaterialDAO {

    public void create(Material material);

    public Material getByID(long id);

    public void update(Material material);

    public void delete(long id);

    public List<Material> list();

    public List<Material> list(Project project);

}