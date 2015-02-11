package com.cebedo.pmsys.task.dao;

import java.util.List;

import com.cebedo.pmsys.task.model.Task;

public interface TaskDAO {

	public void create(Task task);

	public Task getByID(long id);

	public void update(Task task);

	public void delete(long id);

	public List<Task> list();
}
