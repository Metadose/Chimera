package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.bean.EstimationInputBean;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.model.Project;

public interface EstimateService {

    public void rename(Estimate obj, String newKey);

    public void multiSet(Map<String, Estimate> m);

    public String set(Estimate obj);

    public String set(EstimationInputBean estimateInput);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(Estimate obj);

    public Estimate get(String key);

    public Set<String> keys(String pattern);

    public Collection<Estimate> multiGet(Collection<String> keys);

    public List<Estimate> list(Project proj);

    public String computeQuantityEstimate(Estimate estimate);

}
