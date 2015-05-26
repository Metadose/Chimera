package com.cebedo.pmsys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.MaterialDAO;
import com.cebedo.pmsys.enums.MaterialStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Material;
import com.cebedo.pmsys.model.Project;

@Service
public class MaterialServiceImpl implements MaterialService {

    private AuthHelper authHelper = new AuthHelper();
    private MaterialDAO materialDAO;

    public void setMaterialDAO(MaterialDAO materialDAO) {
	this.materialDAO = materialDAO;
    }

    @Override
    @Transactional
    public void create(Material material) {
	if (this.authHelper.isActionAuthorized(material)) {
	    this.materialDAO.create(material);
	}
    }

    @Override
    @Transactional
    public Material getByID(long id) {
	Material material = this.materialDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(material)) {
	    return material;
	}
	return new Material();
    }

    @Override
    @Transactional
    public void update(Material material) {
	if (this.authHelper.isActionAuthorized(material)) {
	    this.materialDAO.update(material);
	}
    }

    @Override
    @Transactional
    public void delete(long id) {
	Material material = this.materialDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(material)) {
	    this.materialDAO.delete(id);
	}
    }

    @Override
    @Transactional
    public List<Material> list() {
	return this.materialDAO.list();
    }

    @Override
    @Transactional
    public Map<String, Object> getMaterialsSummary(Project proj) {
	List<Material> materialsInProj = this.materialDAO.list(proj);

	Map<String, Map<MaterialStatus, Integer>> materialCountMap = new HashMap<String, Map<MaterialStatus, Integer>>();
	double totalCostOfMaterials = 0;

	for (Material material : materialsInProj) {
	    String name = material.getName();

	    // For a specific material,
	    // How many are used? how many are not used?
	    Map<MaterialStatus, Integer> statusCountMap = new HashMap<MaterialStatus, Integer>();
	    MaterialStatus status = MaterialStatus.of(material.getStatus());
	    Integer materialCount = materialCountMap.get(name) == null ? 1
		    : materialCountMap.get(name).get(status) == null ? 1
			    : materialCountMap.get(name).get(status) + 1;
	    statusCountMap.put(status, materialCount);
	    materialCountMap.put(name, statusCountMap);

	    // Total cost of materials.
	    totalCostOfMaterials += material.getPrice();
	}
	Map<String, Object> materialsSummary = new HashMap<String, Object>();
	materialsSummary.put(Material.MATERIALS_SUMMARY_KEY_TOTAL_COST,
		totalCostOfMaterials);
	materialsSummary.put(Material.MATERIALS_SUMMARY_KEY_COUNT,
		materialCountMap);
	return materialsSummary;
    }

}
