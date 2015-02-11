package com.cebedo.pmsys.task.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.task.dao.TaskDAO;
import com.cebedo.pmsys.task.model.Task;

@Service
public class TaskServiceImpl implements TaskService {

	private TaskDAO taskDAO;

	public void setTaskDAO(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	@Override
	@Transactional
	public void create(Task task) {
		this.taskDAO.create(task);
	}

	@Override
	@Transactional
	public Task getByID(long id) {
		return this.taskDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(Task task) {
		this.taskDAO.update(task);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.taskDAO.delete(id);
	}

	@Override
	@Transactional
	public List<Task> list() {
		return this.taskDAO.list();
	}

}
