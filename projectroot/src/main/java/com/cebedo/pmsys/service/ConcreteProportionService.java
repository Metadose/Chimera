package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.ConcreteProportion;

public interface ConcreteProportionService {

    public void rename(ConcreteProportion obj, String newKey);

    public void multiSet(Map<String, ConcreteProportion> m);

    public String set(ConcreteProportion obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(ConcreteProportion obj);

    public ConcreteProportion get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<ConcreteProportion> multiGet(Collection<String> keys);

    public List<ConcreteProportion> list();

}
