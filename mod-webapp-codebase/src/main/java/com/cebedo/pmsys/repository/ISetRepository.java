package com.cebedo.pmsys.repository;

import java.util.Set;

import com.cebedo.pmsys.base.IObjectDomain;

public interface ISetRepository<V extends IObjectDomain> {

	void add(V obj);

	Set<V> members(String key);

}