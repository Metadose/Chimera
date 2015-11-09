package com.cebedo.pmsys.base;

import com.cebedo.pmsys.model.Company;

public interface IObjectDomain extends IObjectBase {

    String getKey();

    public boolean equals(Object obj);

    public int hashCode();

    public Company getCompany();

}
