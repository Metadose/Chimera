package com.cebedo.person.dao;

import java.util.List;

import com.cebedo.person.model.Person;

public interface PersonDAO {

	public void save(Person p);

	public List<Person> list();

}