package com.cebedo.pmsys.field.dao;

import java.util.List;

import com.cebedo.pmsys.field.model.Field;

public interface FieldDAO {

	public void create(Field field);

	public Field getByID(long id);

	public void update(Field field);

	public void delete(long id);

	public List<Field> list();

	public List<Field> listWithAllCollections();
}
