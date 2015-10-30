package com.cebedo.pmsys.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.base.IExpense;
import com.cebedo.pmsys.repository.IExpenseRepo;

public class ExpenseRepoImpl implements IExpenseRepo<IExpense> {

    private RedisTemplate<String, IExpense> redisTemplate;

    @Autowired
    @Qualifier(value = "redisTemplate")
    public void setRedisTemplate(RedisTemplate<String, IExpense> redisTemplate) {
	this.redisTemplate = redisTemplate;
    }

    @Override
    public void rename(IExpense obj, String newKey) {
	this.redisTemplate.rename(obj.getKey(), newKey);
    }

    @Override
    public void delete(Collection<String> keys) {
	this.redisTemplate.delete(keys);
    }

    @Override
    public void set(IExpense obj) {
	this.redisTemplate.opsForValue().set(obj.getKey(), obj);
    }

    @Override
    public void setIfAbsent(IExpense obj) {
	this.redisTemplate.opsForValue().setIfAbsent(obj.getKey(), obj);
    }

    @Override
    public IExpense get(String key) {
	return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> keys(String pattern) {
	return this.redisTemplate.opsForValue().getOperations().keys(pattern);
    }

    @Override
    public void multiSet(Map<String, IExpense> m) {
	this.redisTemplate.opsForValue().multiSet(m);
    }

    @Override
    public List<IExpense> multiGet(Collection<String> keys) {
	return this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void delete(String key) {
	this.redisTemplate.delete(key);
    }

}
