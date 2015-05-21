package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Milestone;

public interface MilestoneDAO {

    public void create(Milestone milestone);

    public Milestone getByID(long id);

    public void update(Milestone milestone);

    public void delete(long id);

    public List<Milestone> list();

}