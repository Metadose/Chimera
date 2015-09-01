package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.model.Project;

public interface PullOutService {

    public String create(PullOut obj, BindingResult result);

    public String delete(String key);

    public PullOut get(String key);

    public List<PullOut> list(Project proj);

}
