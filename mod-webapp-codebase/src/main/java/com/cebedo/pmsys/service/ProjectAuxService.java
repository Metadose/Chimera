package com.cebedo.pmsys.service;

import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.model.Project;

public interface ProjectAuxService {

    public ProjectAux get(String key);

    public ProjectAux get(Delivery delivery);

    public ProjectAux get(Project proj);

}
