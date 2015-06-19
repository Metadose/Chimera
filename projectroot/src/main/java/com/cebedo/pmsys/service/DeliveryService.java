package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.model.Project;

public interface DeliveryService {

    public void rename(Delivery obj, String newKey);

    public void multiSet(Map<String, Delivery> m);

    public String set(Delivery obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(Delivery obj);

    public Delivery get(String uuid);

    public Set<String> keys(String pattern);

    public Collection<Delivery> multiGet(Collection<String> keys);

    public List<Delivery> list(Project proj);

}
