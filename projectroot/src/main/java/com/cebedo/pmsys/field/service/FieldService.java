package com.cebedo.pmsys.field.service;

import java.util.List;

import com.cebedo.pmsys.field.model.Field;

public interface FieldService {

	public void create(Field field);

	public Field getByID(long id);

	public void update(Field field);

	public void delete(long id);

	public List<Field> list();

	public List<Field> listWithAllCollections();
}
