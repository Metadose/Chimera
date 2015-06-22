package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.Unit;

public interface UnitService {

    public void rename(Unit obj, String newKey);

    public void multiSet(Map<String, Unit> m);

    public String set(Unit obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(Unit obj);

    public Unit get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<Unit> multiGet(Collection<String> keys);

    public List<Unit> list();

}
