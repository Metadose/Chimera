package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;

import com.cebedo.pmsys.constants.RegistryCache;
import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.model.Project;

public interface EstimationOutputService {

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String delete(String key, long projectId);

    public EstimationOutput get(String uuid);

    public List<EstimationOutput> listDesc(Project proj);

    public List<EstimationOutput> listDesc(Project proj, boolean override);

}
