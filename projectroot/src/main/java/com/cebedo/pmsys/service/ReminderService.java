package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.Reminder;

public interface ReminderService {

    public void create(Reminder reminder);

    public Reminder getByID(long id);

    public void update(Reminder reminder);

    public void delete(long id);

    public List<Reminder> list();

}
