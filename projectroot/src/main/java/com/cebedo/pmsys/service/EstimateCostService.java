package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.model.Project;

public interface EstimateCostService {

    public String delete(String key);

    public EstimateCost get(String uuid);

    public List<EstimateCost> list(Project proj);

    public String set(EstimateCost cost);

}