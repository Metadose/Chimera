package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.ConcreteEstimationSummary;
import com.cebedo.pmsys.model.Project;

public interface ConcreteEstimationSummaryService {

    public void rename(ConcreteEstimationSummary obj, String newKey);

    public void multiSet(Map<String, ConcreteEstimationSummary> m);

    public String set(ConcreteEstimationSummary obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(ConcreteEstimationSummary obj);

    public ConcreteEstimationSummary get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<ConcreteEstimationSummary> multiGet(
	    Collection<String> keys);

    public List<ConcreteEstimationSummary> list(Project proj);

}
