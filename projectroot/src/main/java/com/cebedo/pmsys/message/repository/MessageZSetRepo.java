package com.cebedo.pmsys.message.repository;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.message.domain.Message;
import com.cebedo.pmsys.system.redis.repository.ZSetRepository;

public class MessageZSetRepo implements ZSetRepository<Message> {

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
