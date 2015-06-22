package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.MaterialCategory;

public interface MaterialCategoryService {

    public void rename(MaterialCategory obj, String newKey);

    public void multiSet(Map<String, MaterialCategory> m);

    public String set(MaterialCategory obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(MaterialCategory obj);

    public MaterialCategory get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<MaterialCategory> multiGet(Collection<String> keys);

    public List<MaterialCategory> list();

}
