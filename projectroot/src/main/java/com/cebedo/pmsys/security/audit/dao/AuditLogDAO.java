package com.cebedo.pmsys.security.audit.dao;

import java.util.List;

import com.cebedo.pmsys.security.audit.model.AuditLog;

public interface AuditLogDAO {

	public void create(AuditLog auditLog);

	public AuditLog getByID(long id);

	public void delete(long id);

	public List<AuditLog> list(Long companyID);

}
