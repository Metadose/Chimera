package com.cebedo.pmsys.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.domain.CHBFootingMixture;

public class CHBFootingMixtureValueRepo implements
	IValueRepository<CHBFootingMixture> {

    private RedisTemplate<String, CHBFootingMixture> redisTemplate;

    public void setRedisTemplate(
	    RedisTemplate<String, CHBFootingMixture> redisTemplate) {
	this.redisTemplate = redisTemplate;
    }

    @Override
    public void rename(CHBFootingMixture obj, String newKey) {
	this.redisTemplate.rename(obj.getKey(), newKey);
    }

    @Override
    public void delete(Collection<String> keys) {
	this.redisTemplate.delete(keys);
    }

    @Override
    public void set(CHBFootingMixture obj) {
	this.redisTemplate.opsForValue().set(obj.getKey(), obj);
    }

    @Override
    public void setIfAbsent(CHBFootingMixture obj) {
	this.redisTemplate.opsForValue().setIfAbsent(obj.getKey(), obj);
    }

    @Override
    public CHBFootingMixture get(String key) {
	return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> keys(String pattern) {
	return this.redisTemplate.opsForValue().getOperations().keys(pattern);
    }

    @Override
    public void multiSet(Map<String, CHBFootingMixture> m) {
	this.redisTemplate.opsForValue().multiSet(m);
    }

    @Override
    public List<CHBFootingMixture> multiGet(Collection<String> keys) {
	return this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void delete(String key) {
	this.redisTemplate.delete(key);
    }

}
