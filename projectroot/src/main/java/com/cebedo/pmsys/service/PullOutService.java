package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.model.Project;

public interface PullOutService {

    public String create(PullOut obj);

    public String delete(String key);

    public PullOut get(String key);

    public List<PullOut> list(Project proj);

    public String update(PullOut pullout);

}
