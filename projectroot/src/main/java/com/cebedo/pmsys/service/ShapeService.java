package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.Shape;

public interface ShapeService {

    public void rename(Shape obj, String newKey);

    public void multiSet(Map<String, Shape> m);

    public String set(Shape obj);

    public void delete(Collection<String> keys);

    public void delete(String key);

    public void setIfAbsent(Shape obj);

    public Shape get(String key);

    public Set<String> keys(String pattern);

    public Collection<Shape> multiGet(Collection<String> keys);

    public List<Shape> list();

    public List<String> getAllVariableNames(String formula);

}
