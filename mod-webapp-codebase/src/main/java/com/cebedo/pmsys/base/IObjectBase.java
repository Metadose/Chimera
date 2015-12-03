package com.cebedo.pmsys.base;

import java.io.Serializable;

public interface IObjectBase extends Serializable, Cloneable {

    public String getName();

    public String getObjectName();

}
