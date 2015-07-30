package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.model.Project;

public interface EstimationOutputService {

    public void rename(EstimationOutput obj, String newKey);

    public void multiSet(Map<String, EstimationOutput> m);

    public String set(EstimationOutput obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(EstimationOutput obj);

    public EstimationOutput get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<EstimationOutput> multiGet(Collection<String> keys);

    public List<EstimationOutput> list(Project proj);

}
