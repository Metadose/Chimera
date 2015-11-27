package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.AuditLog;

public interface AuditLogService {

    public List<AuditLog> list();

    public void create(AuditLog audit);

}
