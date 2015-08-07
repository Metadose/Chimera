package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.model.Project;

public interface EstimationOutputService {

    public String delete(String key);

    public EstimationOutput get(String uuid);

    public List<EstimationOutput> list(Project proj);

}
