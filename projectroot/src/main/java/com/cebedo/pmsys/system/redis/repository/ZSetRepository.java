package com.cebedo.pmsys.system.redis.repository;

import java.util.Set;

import com.cebedo.pmsys.notification.domain.Notification;
import com.cebedo.pmsys.system.redis.domain.IDomainObject;

public interface ZSetRepository<V extends IDomainObject> {

	void add(V obj);

	Set<Notification> rangeByScore(String key, long min, long max);

	void removeRangeByScore(String key, long min, long max);

}