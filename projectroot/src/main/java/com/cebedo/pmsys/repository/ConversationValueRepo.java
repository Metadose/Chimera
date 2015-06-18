package com.cebedo.pmsys.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.domain.Conversation;

public class ConversationValueRepo implements IValueRepository<Conversation> {

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

    @Override
    public void delete(Collection<String> keys) {
	this.redisTemplate.delete(keys);
    }

    @Override
    public void multiSet(Map<String, Conversation> m) {
	this.redisTemplate.opsForValue().multiSet(m);
    }

    @Override
    public Collection<Conversation> multiGet(Collection<String> keys) {
	return this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void delete(String key) {
	this.redisTemplate.delete(key);
    }

}
