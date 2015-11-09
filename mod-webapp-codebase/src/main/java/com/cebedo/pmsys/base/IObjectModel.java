package com.cebedo.pmsys.base;

import com.cebedo.pmsys.model.Company;

public interface IObjectModel extends IObjectBase {

    public long getId();

    public String getTableName();

    public Company getCompany();

}
