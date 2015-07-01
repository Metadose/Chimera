package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.EstimationAllowance;

public interface EstimationAllowanceService {

    public void rename(EstimationAllowance obj, String newKey);

    public void multiSet(Map<String, EstimationAllowance> m);

    public String set(EstimationAllowance obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(EstimationAllowance obj);

    public EstimationAllowance get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<EstimationAllowance> multiGet(Collection<String> keys);

    public List<EstimationAllowance> list();

}
