package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.CHBFootingDimension;

public interface CHBFootingDimensionService {

    public void rename(CHBFootingDimension obj, String newKey);

    public void multiSet(Map<String, CHBFootingDimension> m);

    public String set(CHBFootingDimension obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(CHBFootingDimension obj);

    public CHBFootingDimension get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<CHBFootingDimension> multiGet(Collection<String> keys);

    public List<CHBFootingDimension> list();

}
