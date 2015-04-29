package com.cebedo.pmsys.system.redis.repository;

import java.util.List;

import com.cebedo.pmsys.system.redis.domain.IDomainObject;

public interface ListRepository<V extends IDomainObject> {

	void rightPush(V obj);

	List<V> range(String key);

}