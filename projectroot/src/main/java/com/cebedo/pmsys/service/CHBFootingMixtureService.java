package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.CHBFootingMixture;

public interface CHBFootingMixtureService {

    public void rename(CHBFootingMixture obj, String newKey);

    public void multiSet(Map<String, CHBFootingMixture> m);

    public String set(CHBFootingMixture obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(CHBFootingMixture obj);

    public CHBFootingMixture get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<CHBFootingMixture> multiGet(Collection<String> keys);

    public List<CHBFootingMixture> list();

}
