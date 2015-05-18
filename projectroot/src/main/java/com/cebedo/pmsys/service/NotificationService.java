package com.cebedo.pmsys.service;

import java.util.Set;

import com.cebedo.pmsys.domain.Notification;

public interface NotificationService {

	public void add(Notification obj);

	public Set<Notification> rangeByScore(String key, long min, long max);

	public void removeRangeByScore(String key, long min, long max);

}
