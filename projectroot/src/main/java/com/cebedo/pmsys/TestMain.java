package com.cebedo.pmsys;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;

public class TestMain {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"hibernate4.xml");

		ProjectDAO projectDAO = context.getBean(ProjectDAO.class);

		Project person = new Project();
		person.setName("test");
		person.setStatus(1);

		projectDAO.save(person);

		System.out.println("Person::" + person);

		List<Project> list = projectDAO.list();

		for (Project p : list) {
			System.out.println("Person List::" + p);
		}
		// close resources
		context.close();
	}
}