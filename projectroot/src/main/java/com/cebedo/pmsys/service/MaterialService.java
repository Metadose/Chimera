package com.cebedo.pmsys.service;

import java.util.List;
import java.util.Map;

import com.cebedo.pmsys.model.Material;
import com.cebedo.pmsys.model.Project;

public interface MaterialService {

    public void create(Material material);

    public Material getByID(long id);

    public void update(Material material);

    public void delete(long id);

    public List<Material> list();

    public Map<String, Object> getMaterialsSummary(Project proj);

}
