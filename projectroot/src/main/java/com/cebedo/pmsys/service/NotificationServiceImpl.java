package com.cebedo.pmsys.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.domain.Notification;
import com.cebedo.pmsys.repository.NotificationZSetRepo;

@Service
public class NotificationServiceImpl implements NotificationService {

	private NotificationZSetRepo notificationZSetRepo;

	public void setNotificationZSetRepo(
			NotificationZSetRepo notificationZSetRepo) {
		this.notificationZSetRepo = notificationZSetRepo;
	}

	@Transactional
	@Override
	public void add(Notification obj) {
		this.notificationZSetRepo.add(obj);
	}

	@Transactional
	@Override
	public Set<Notification> rangeByScore(String key, long min, long max) {
		return this.notificationZSetRepo.rangeByScore(key, min, max);
	}

	@Transactional
	@Override
	public void removeRangeByScore(String key, long min, long max) {
		this.notificationZSetRepo.removeRangeByScore(key, min, max);
	}

}
