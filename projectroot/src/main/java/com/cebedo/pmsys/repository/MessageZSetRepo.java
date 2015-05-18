package com.cebedo.pmsys.repository;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.domain.Message;

public class MessageZSetRepo implements IZSetRepository<Message> {

	private RedisTemplate<String, Message> redisTemplate;

	public void setRedisTemplate(RedisTemplate<String, Message> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void add(Message obj) {
		this.redisTemplate.opsForZSet().add(obj.getKey(), obj,
				obj.getTimestamp().getTime());
	}

	@Override
	public Set<Message> rangeByScore(String key, long min, long max) {
		return this.redisTemplate.opsForZSet().rangeByScore(key, min, max);
	}

	@Override
	public void removeRangeByScore(String key, long min, long max) {
		this.redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
	}

}
