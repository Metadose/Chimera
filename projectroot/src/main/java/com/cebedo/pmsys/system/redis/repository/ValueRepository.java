package com.cebedo.pmsys.system.redis.repository;

import java.util.Set;

import com.cebedo.pmsys.system.redis.domain.IDomainObject;

public interface ValueRepository<V extends IDomainObject> {

	void set(V obj);

	V get(String key);

	Set<String> keys(String pattern);

}