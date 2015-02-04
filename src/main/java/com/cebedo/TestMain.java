package com.cebedo;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cebedo.person.dao.PersonDAO;
import com.cebedo.person.model.Person;

public class TestMain {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"spring4.xml");

		PersonDAO personDAO = context.getBean(PersonDAO.class);

		Person person = new Person();
		person.setName("test");
		person.setCountry("test23");

		personDAO.save(person);

		System.out.println("Person::" + person);

		List<Person> list = personDAO.list();

		for (Person p : list) {
			System.out.println("Person List::" + p);
		}
		// close resources
		context.close();
	}
}