package com.cebedo.pmsys.domain;

import java.util.Map;
import java.util.UUID;

import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;

public class MasonryEstimationSummary implements IDomainObject {

    private static final long serialVersionUID = -946543461997147334L;

    /**
     * Key parts.
     */
    private Company company;
    private Project project;
    private UUID uuid;

    /**
     * Computed specs.
     */
    private double totalPiecesCHB;
    private double costPerPieceCHB;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    @Override
    public String getKey() {
	// TODO Auto-generated method stub
	return null;
    }

}
