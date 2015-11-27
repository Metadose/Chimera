package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.model.Project;

public interface MaterialService {

    public String create(Material obj, BindingResult result);

    public String delete(String key);

    public Material get(String key);

    public List<Material> listDesc(Delivery delivery);

    public List<Material> listDesc(Project proj);

    public String update(Material material, BindingResult result);

    public List<Material> listDesc(Project proj, boolean override);

}
