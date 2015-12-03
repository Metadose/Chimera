package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.constants.RegistryCache;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.model.Project;

public interface PullOutService {

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#obj.project.id")
    public String create(PullOut obj, BindingResult result);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String delete(String key, long projectId);

    public PullOut get(String key);

    public List<PullOut> listDesc(Project proj);

    public List<PullOut> listDesc(Project proj, boolean override);

}
