package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.PlasterMixture;

public interface PlasterMixtureService {

    public void rename(PlasterMixture obj, String newKey);

    public void multiSet(Map<String, PlasterMixture> m);

    public String set(PlasterMixture obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(PlasterMixture obj);

    public PlasterMixture get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<PlasterMixture> multiGet(Collection<String> keys);

    public List<PlasterMixture> list();

}
