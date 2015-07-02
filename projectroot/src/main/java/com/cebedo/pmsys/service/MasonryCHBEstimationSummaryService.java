package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.MasonryCHBEstimationSummary;
import com.cebedo.pmsys.model.Project;

public interface MasonryCHBEstimationSummaryService {

    public void rename(MasonryCHBEstimationSummary obj, String newKey);

    public void multiSet(Map<String, MasonryCHBEstimationSummary> m);

    public String set(MasonryCHBEstimationSummary obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(MasonryCHBEstimationSummary obj);

    public MasonryCHBEstimationSummary get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<MasonryCHBEstimationSummary> multiGet(Collection<String> keys);

    public List<MasonryCHBEstimationSummary> list(Project proj);

}
