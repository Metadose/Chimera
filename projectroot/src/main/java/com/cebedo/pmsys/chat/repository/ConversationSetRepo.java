package com.cebedo.pmsys.chat.repository;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.chat.domain.Conversation;
import com.cebedo.pmsys.system.redis.repository.SetRepository;

public class ConversationSetRepo implements SetRepository<Conversation> {

	private RedisTemplate<String, Conversation> redisTemplate;

	public void setRedisTemplate(
			RedisTemplate<String, Conversation> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void add(Conversation obj) {
		this.redisTemplate.opsForSet().add(obj.getKey(), obj);
	}

	@Override
	public Set<Conversation> members(String key) {
		return this.redisTemplate.opsForSet().members(key);
	}

}
