package com.cebedo.pmsys.system.redis.repository;

import java.util.Set;

import com.cebedo.pmsys.system.redis.domain.IDomainObject;

public interface SetRepository<V extends IDomainObject> {

	void add(V obj);

	Set<V> members(String key);

}