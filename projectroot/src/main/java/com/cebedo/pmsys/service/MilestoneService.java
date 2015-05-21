package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.Milestone;

public interface MilestoneService {

    public void create(Milestone milestone);

    public Milestone getByID(long id);

    public void update(Milestone milestone);

    public void delete(long id);

    public List<Milestone> list();

}
