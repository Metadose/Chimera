package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Staff;

public interface StaffDAO {

    public void create(Staff staff);

    public Staff getByID(long id);

    public Staff getWithAllCollectionsByID(long id);

    public void update(Staff staff);

    public void delete(long id);

    public List<Staff> list(Long companyID);

    public List<Staff> listWithAllCollections(Long companyID);

    public String getNameByID(long staffID);

    public List<Staff> listStaffFromCache(Long companyID);

    public Staff getStaffByName(Staff staff);

}
