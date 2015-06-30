package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.MasonryEstimationSummary;
import com.cebedo.pmsys.model.Project;

public interface MasonryEstimationSummaryService {

    public void rename(MasonryEstimationSummary obj, String newKey);

    public void multiSet(Map<String, MasonryEstimationSummary> m);

    public String set(MasonryEstimationSummary obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(MasonryEstimationSummary obj);

    public MasonryEstimationSummary get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<MasonryEstimationSummary> multiGet(Collection<String> keys);

    public List<MasonryEstimationSummary> list(Project proj);

}
