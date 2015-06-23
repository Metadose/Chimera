package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.ConcreteMixingRatio;

public interface ConcreteMixingRatioService {

    public void rename(ConcreteMixingRatio obj, String newKey);

    public void multiSet(Map<String, ConcreteMixingRatio> m);

    public String set(ConcreteMixingRatio obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(ConcreteMixingRatio obj);

    public ConcreteMixingRatio get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<ConcreteMixingRatio> multiGet(Collection<String> keys);

    public List<ConcreteMixingRatio> list();

}
