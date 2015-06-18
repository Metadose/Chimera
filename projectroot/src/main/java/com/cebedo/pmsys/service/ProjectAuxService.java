package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.ProjectAux;

public interface ProjectAuxService {

    public void rename(ProjectAux obj, String newKey);

    public void multiSet(Map<String, ProjectAux> m);

    public void set(ProjectAux obj);

    public void delete(Collection<String> keys);

    public void delete(String key);

    public void setIfAbsent(ProjectAux obj);

    public ProjectAux get(String key);

    public Set<String> keys(String pattern);

    public Collection<ProjectAux> multiGet(Collection<String> keys);

}
