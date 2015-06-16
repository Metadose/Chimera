package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.Material;

public interface MaterialService {

    public void rename(Material obj, String newKey);

    public void multiSet(Map<String, Material> m);

    public void set(Material obj);

    public void delete(Collection<String> keys);

    public void setIfAbsent(Material obj);

    public Material get(String key);

    public Set<String> keys(String pattern);

    public Collection<Material> multiGet(Collection<String> keys);

}
