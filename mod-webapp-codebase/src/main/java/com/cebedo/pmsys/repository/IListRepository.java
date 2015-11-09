package com.cebedo.pmsys.repository;

import java.util.List;

import com.cebedo.pmsys.base.IObjectDomain;

public interface IListRepository<V extends IObjectDomain> {

	void rightPush(V obj);

	List<V> range(String key);

}