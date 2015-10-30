package com.cebedo.pmsys.base;

import java.io.Serializable;

import com.cebedo.pmsys.model.Company;

public interface IObjectBase extends Serializable {

    public String getName();

    public String getObjectName();

    public Company getCompany();

}
