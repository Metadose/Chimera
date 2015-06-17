package com.cebedo.pmsys.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.domain.Delivery;

public class DeliveryValueRepo implements IValueRepository<Delivery> {

    private RedisTemplate<String, Delivery> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Delivery> redisTemplate) {
	this.redisTemplate = redisTemplate;
    }

    @Override
    public void rename(Delivery obj, String newKey) {
	this.redisTemplate.rename(obj.getKey(), newKey);
    }

    @Override
    public void delete(Collection<String> keys) {
	this.redisTemplate.delete(keys);
    }

    @Override
    public void set(Delivery obj) {
	this.redisTemplate.opsForValue().set(obj.getKey(), obj);
    }

    @Override
    public void setIfAbsent(Delivery obj) {
	this.redisTemplate.opsForValue().setIfAbsent(obj.getKey(), obj);
    }

    @Override
    public Delivery get(String key) {
	return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> keys(String pattern) {
	return this.redisTemplate.opsForValue().getOperations().keys(pattern);
    }

    @Override
    public void multiSet(Map<String, Delivery> m) {
	this.redisTemplate.opsForValue().multiSet(m);
    }

    @Override
    public List<Delivery> multiGet(Collection<String> keys) {
	return this.redisTemplate.opsForValue().multiGet(keys);
    }

}
