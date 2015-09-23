package com.cebedo.pmsys.repository;

import java.util.List;

import com.cebedo.pmsys.domain.IDomainObject;

public interface IListRepository<V extends IDomainObject> {

	void rightPush(V obj);

	List<V> range(String key);

}