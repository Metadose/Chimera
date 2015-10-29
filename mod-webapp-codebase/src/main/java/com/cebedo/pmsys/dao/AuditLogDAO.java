package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.AuditLog;

public interface AuditLogDAO {

    public void deleteAll(long userID);

    public void create(AuditLog auditLog);

    public AuditLog getByID(long id);

    public void delete(long id);

    public List<AuditLog> list(Long companyID);

}
