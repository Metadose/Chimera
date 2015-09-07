package com.cebedo.pmsys.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.domain.EstimateCost;

public class EstimateCostValueRepo implements IValueRepository<EstimateCost> {

    private RedisTemplate<String, EstimateCost> redisTemplate;

    @Autowired(required = true)
    @Qualifier(value = "redisTemplate")
    public void setRedisTemplate(RedisTemplate<String, EstimateCost> redisTemplate) {
	this.redisTemplate = redisTemplate;
    }

    @Override
    public void rename(EstimateCost obj, String newKey) {
	this.redisTemplate.rename(obj.getKey(), newKey);
    }

    @Override
    public void delete(Collection<String> keys) {
	this.redisTemplate.delete(keys);
    }

    @Override
    public void set(EstimateCost obj) {
	this.redisTemplate.opsForValue().set(obj.getKey(), obj);
    }

    @Override
    public void setIfAbsent(EstimateCost obj) {
	this.redisTemplate.opsForValue().setIfAbsent(obj.getKey(), obj);
    }

    @Override
    public EstimateCost get(String key) {
	return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> keys(String pattern) {
	return this.redisTemplate.opsForValue().getOperations().keys(pattern);
    }

    @Override
    public void multiSet(Map<String, EstimateCost> m) {
	this.redisTemplate.opsForValue().multiSet(m);
    }

    @Override
    public List<EstimateCost> multiGet(Collection<String> keys) {
	return this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void delete(String key) {
	this.redisTemplate.delete(key);
    }

}
