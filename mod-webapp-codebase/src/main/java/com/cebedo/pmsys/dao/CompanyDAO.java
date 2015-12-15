package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.base.IObjectModel;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;

public interface CompanyDAO {

    public void executeDelete(IObjectModel model, long companyId);

    public void delete(Company company);

    public void create(Company company);

    public Company getByID(long id);

    public Company getByIDWithLazyCollections(long id);

    public void update(Company company);

    public List<Company> list(Long companyID);

    /**
     * Use session attributes rather than using this function.
     * 
     * @param objTable
     * @param objKeyCol
     * @param objID
     * @return
     */
    @Deprecated
    public long getCompanyIDByObjID(String objTable, String objKeyCol, long objID);

    /**
     * Use session attributes rather than using this function.
     * 
     * @param objTable
     * @param objKeyCol
     * @param objID
     * @return
     */
    @Deprecated
    public Company getCompanyByObjID(String objTable, String objKeyCol, long objID);

    public List<AuditLog> logs(Long companyID);

}
