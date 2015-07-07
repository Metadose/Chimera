package com.cebedo.pmsys.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.domain.CHBHorizontalReinforcement;

public class CHBHorizontalReinforcementValueRepo implements
	IValueRepository<CHBHorizontalReinforcement> {

    private RedisTemplate<String, CHBHorizontalReinforcement> redisTemplate;

    public void setRedisTemplate(
	    RedisTemplate<String, CHBHorizontalReinforcement> redisTemplate) {
	this.redisTemplate = redisTemplate;
    }

    @Override
    public void rename(CHBHorizontalReinforcement obj, String newKey) {
	this.redisTemplate.rename(obj.getKey(), newKey);
    }

    @Override
    public void delete(Collection<String> keys) {
	this.redisTemplate.delete(keys);
    }

    @Override
    public void set(CHBHorizontalReinforcement obj) {
	this.redisTemplate.opsForValue().set(obj.getKey(), obj);
    }

    @Override
    public void setIfAbsent(CHBHorizontalReinforcement obj) {
	this.redisTemplate.opsForValue().setIfAbsent(obj.getKey(), obj);
    }

    @Override
    public CHBHorizontalReinforcement get(String key) {
	return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> keys(String pattern) {
	return this.redisTemplate.opsForValue().getOperations().keys(pattern);
    }

    @Override
    public void multiSet(Map<String, CHBHorizontalReinforcement> m) {
	this.redisTemplate.opsForValue().multiSet(m);
    }

    @Override
    public List<CHBHorizontalReinforcement> multiGet(Collection<String> keys) {
	return this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void delete(String key) {
	this.redisTemplate.delete(key);
    }

}
