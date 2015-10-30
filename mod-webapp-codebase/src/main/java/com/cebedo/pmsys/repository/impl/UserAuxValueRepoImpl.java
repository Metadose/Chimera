package com.cebedo.pmsys.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.domain.UserAux;
import com.cebedo.pmsys.repository.IValueRepository;

public class UserAuxValueRepoImpl implements IValueRepository<UserAux> {

    private RedisTemplate<String, UserAux> redisTemplate;

    @Autowired(required = true)
    @Qualifier(value = "redisTemplate")
    public void setRedisTemplate(RedisTemplate<String, UserAux> redisTemplate) {
	this.redisTemplate = redisTemplate;
    }

    @Override
    public void rename(UserAux obj, String newKey) {
	this.redisTemplate.rename(obj.getKey(), newKey);
    }

    @Override
    public void delete(Collection<String> keys) {
	this.redisTemplate.delete(keys);
    }

    @Override
    public void set(UserAux obj) {
	this.redisTemplate.opsForValue().set(obj.getKey(), obj);
    }

    @Override
    public void setIfAbsent(UserAux obj) {
	this.redisTemplate.opsForValue().setIfAbsent(obj.getKey(), obj);
    }

    @Override
    public UserAux get(String key) {
	return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> keys(String pattern) {
	return this.redisTemplate.opsForValue().getOperations().keys(pattern);
    }

    @Override
    public void multiSet(Map<String, UserAux> m) {
	this.redisTemplate.opsForValue().multiSet(m);
    }

    @Override
    public List<UserAux> multiGet(Collection<String> keys) {
	return this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void delete(String key) {
	this.redisTemplate.delete(key);
    }

}
