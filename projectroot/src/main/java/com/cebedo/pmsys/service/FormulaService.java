package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.Formula;

public interface FormulaService {

    public void rename(Formula obj, String newKey);

    public void multiSet(Map<String, Formula> m);

    public String set(Formula obj);

    public void delete(Collection<String> keys);

    public void setIfAbsent(Formula obj);

    public Formula get(String key);

    public Set<String> keys(String pattern);

    public Collection<Formula> multiGet(Collection<String> keys);

    public List<Formula> list();

}
