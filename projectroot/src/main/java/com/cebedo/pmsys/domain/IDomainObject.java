package com.cebedo.pmsys.domain;

import java.io.Serializable;
import java.util.Map;

public interface IDomainObject extends Serializable {

    public Map<String, Object> getExtMap();

    public void setExtMap(Map<String, Object> extMap);

    String getKey();

}
