package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.model.Project;

public interface MaterialService {

    public void rename(Material obj, String newKey);

    public void multiSet(Map<String, Material> m);

    public String set(Material obj);

    public void delete(Collection<String> keys);

    public void delete(String key);

    public void setIfAbsent(Material obj);

    public Material get(String key);

    public Set<String> keys(String pattern);

    public Collection<Material> multiGet(Collection<String> keys);

    public List<Material> list(Delivery delivery);

    public List<Material> list(Project proj);

}
