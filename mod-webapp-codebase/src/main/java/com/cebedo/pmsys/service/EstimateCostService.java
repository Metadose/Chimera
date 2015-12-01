package com.cebedo.pmsys.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.RegistryCache;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.model.Project;

public interface EstimateCostService {

    public HSSFWorkbook exportXLS(long projID);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String createMassCosts(List<EstimateCost> costs, BindingResult result, long projectId);

    public List<EstimateCost> convertExcelToCostList(MultipartFile multipartFile, Project project);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String delete(String key, long projectId);

    public EstimateCost get(String uuid);

    public List<EstimateCost> list(Project proj);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#obj.project.id")
    public String set(EstimateCost obj, BindingResult result);

    public List<EstimateCost> list(Project proj, boolean override);

}