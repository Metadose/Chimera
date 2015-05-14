package com.cebedo.pmsys.system.redis.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.system.redis.domain.IDomainObject;

public interface ValueRepository<V extends IDomainObject> {

	void rename(V obj, String newKey);

	void multiSet(Map<String, V> m);

	void set(V obj);

	void delete(Collection<String> keys);

	void setIfAbsent(V obj);

	V get(String key);

	Set<String> keys(String pattern);

}