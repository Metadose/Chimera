package com.cebedo.pmsys.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.IDomainObject;

public interface IValueRepository<V extends IDomainObject> {

    void rename(V obj, String newKey);

    void multiSet(Map<String, V> m);

    void set(V obj);

    void delete(Collection<String> keys);

    void delete(String key);

    void setIfAbsent(V obj);

    V get(String key);

    Set<String> keys(String pattern);

    Collection<V> multiGet(Collection<String> keys);

}