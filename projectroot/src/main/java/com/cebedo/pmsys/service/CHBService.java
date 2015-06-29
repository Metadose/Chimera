package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.CHB;

public interface CHBService {

    public void rename(CHB obj, String newKey);

    public void multiSet(Map<String, CHB> m);

    public String set(CHB obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(CHB obj);

    public CHB get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<CHB> multiGet(Collection<String> keys);

    public List<CHB> list();

}
