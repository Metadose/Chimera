package com.cebedo.pmsys.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.domain.BlockLayingMixture;

public class BlockLayingMixtureValueRepo implements
	IValueRepository<BlockLayingMixture> {

    private RedisTemplate<String, BlockLayingMixture> redisTemplate;

    public void setRedisTemplate(
	    RedisTemplate<String, BlockLayingMixture> redisTemplate) {
	this.redisTemplate = redisTemplate;
    }

    @Override
    public void rename(BlockLayingMixture obj, String newKey) {
	this.redisTemplate.rename(obj.getKey(), newKey);
    }

    @Override
    public void delete(Collection<String> keys) {
	this.redisTemplate.delete(keys);
    }

    @Override
    public void set(BlockLayingMixture obj) {
	this.redisTemplate.opsForValue().set(obj.getKey(), obj);
    }

    @Override
    public void setIfAbsent(BlockLayingMixture obj) {
	this.redisTemplate.opsForValue().setIfAbsent(obj.getKey(), obj);
    }

    @Override
    public BlockLayingMixture get(String key) {
	return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public Set<String> keys(String pattern) {
	return this.redisTemplate.opsForValue().getOperations().keys(pattern);
    }

    @Override
    public void multiSet(Map<String, BlockLayingMixture> m) {
	this.redisTemplate.opsForValue().multiSet(m);
    }

    @Override
    public List<BlockLayingMixture> multiGet(Collection<String> keys) {
	return this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void delete(String key) {
	this.redisTemplate.delete(key);
    }

}
