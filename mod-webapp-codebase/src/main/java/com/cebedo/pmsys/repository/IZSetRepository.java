package com.cebedo.pmsys.repository;

import java.util.Set;

import com.cebedo.pmsys.base.IDomainObject;

public interface IZSetRepository<V extends IDomainObject> {

	void add(V obj);

	Set<V> rangeByScore(String key, long min, long max);

	void removeRangeByScore(String key, long min, long max);

}