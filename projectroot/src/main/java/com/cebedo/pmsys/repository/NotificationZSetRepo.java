package com.cebedo.pmsys.repository;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.domain.Notification;

public class NotificationZSetRepo implements IZSetRepository<Notification> {

	private RedisTemplate<String, Notification> redisTemplate;

	public void setRedisTemplate(
			RedisTemplate<String, Notification> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void add(Notification obj) {
		this.redisTemplate.opsForZSet().add(obj.getKey(), obj,
				obj.getTimestamp().getTime());
	}

	@Override
	public Set<Notification> rangeByScore(String key, long min, long max) {
		return this.redisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	@Override
	public void removeRangeByScore(String key, long min, long max) {
		this.redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
	}

}
