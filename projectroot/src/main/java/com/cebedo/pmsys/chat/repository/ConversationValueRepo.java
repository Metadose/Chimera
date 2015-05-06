package com.cebedo.pmsys.chat.repository;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.chat.domain.Conversation;
import com.cebedo.pmsys.system.redis.repository.ValueRepository;

public class ConversationValueRepo implements ValueRepository<Conversation> {

	private RedisTemplate<String, Conversation> redisTemplate;

	public void setRedisTemplate(
			RedisTemplate<String, Conversation> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void rename(Conversation obj, String newKey) {
		this.redisTemplate.rename(obj.getKey(), newKey);
	}

	@Override
	public void set(Conversation obj) {
		this.redisTemplate.opsForValue().set(obj.getKey(), obj);
	}

	@Override
	public void setIfAbsent(Conversation obj) {
		this.redisTemplate.opsForValue().setIfAbsent(obj.getKey(), obj);
	}

	@Override
	public Conversation get(String key) {
		return this.redisTemplate.opsForValue().get(key);
	}

	@Override
	public Set<String> keys(String pattern) {
		return this.redisTemplate.opsForValue().getOperations().keys(pattern);
	}

}
