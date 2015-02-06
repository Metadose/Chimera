package com.cebedo.pmsys;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;

public class TestMain {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"hibernate4.xml");

		ProjectDAO ProjectDAO = context.getBean(ProjectDAO.class);

		Project Project = new Project();
		Project.setName("test111");
		Project.setCountry("test2311");

		ProjectDAO.save(Project);

		System.out.println("Project::" + Project);

		List<Project> list = ProjectDAO.list();

		for (Project p : list) {
			System.out.println("Project List::" + p);
		}
		// close resources
		context.close();
	}
}