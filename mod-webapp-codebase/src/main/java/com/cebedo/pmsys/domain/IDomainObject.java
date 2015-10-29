package com.cebedo.pmsys.domain;

import java.io.Serializable;

public interface IDomainObject extends Serializable {

    String getKey();

    public boolean equals(Object obj);

    public int hashCode();

}
