package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.RegistryCache;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.model.Project;

public interface MaterialService {

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#obj.project.id")
    public String create(Material obj, BindingResult result);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String delete(String key, long projectId);

    public Material get(String key);

    public List<Material> listDesc(Delivery delivery);

    public List<Material> listDesc(Project proj);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#material.project.id")
    public String update(Material material, BindingResult result);

    public List<Material> listDesc(Project proj, boolean override);

    public List<Material> convertExcelToMaterials(MultipartFile multipartFile, Project project);
}
