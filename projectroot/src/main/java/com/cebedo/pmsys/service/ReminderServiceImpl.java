package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.ReminderDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Reminder;

@Service
public class ReminderServiceImpl implements ReminderService {

    private AuthHelper authHelper = new AuthHelper();
    private ReminderDAO reminderDAO;

    public void setReminderDAO(ReminderDAO reminderDAO) {
	this.reminderDAO = reminderDAO;
    }

    @Override
    @Transactional
    public void create(Reminder reminder) {
	if (this.authHelper.isActionAuthorized(reminder)) {
	    this.reminderDAO.create(reminder);
	}
    }

    @Override
    @Transactional
    public Reminder getByID(long id) {
	Reminder reminder = this.reminderDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(reminder)) {
	    return reminder;
	}
	return new Reminder();
    }

    @Override
    @Transactional
    public void update(Reminder reminder) {
	if (this.authHelper.isActionAuthorized(reminder)) {
	    this.reminderDAO.update(reminder);
	}
    }

    @Override
    @Transactional
    public void delete(long id) {
	Reminder reminder = this.reminderDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(reminder)) {
	    this.reminderDAO.delete(id);
	}
    }

    @Override
    @Transactional
    public List<Reminder> list() {
	return this.reminderDAO.list();
    }

}
