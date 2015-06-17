package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.Delivery;

public interface DeliveryService {

    public void rename(Delivery obj, String newKey);

    public void multiSet(Map<String, Delivery> m);

    public void set(Delivery obj);

    public void delete(Collection<String> keys);

    public void setIfAbsent(Delivery obj);

    public Delivery get(String key);

    public Set<String> keys(String pattern);

    public Collection<Delivery> multiGet(Collection<String> keys);

}
