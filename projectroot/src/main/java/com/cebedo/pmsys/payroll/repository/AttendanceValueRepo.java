package com.cebedo.pmsys.payroll.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.cebedo.pmsys.payroll.domain.Attendance;
import com.cebedo.pmsys.system.redis.repository.ValueRepository;

public class AttendanceValueRepo implements ValueRepository<Attendance> {

	private RedisTemplate<String, Attendance> redisTemplate;

	public void setRedisTemplate(RedisTemplate<String, Attendance> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void rename(Attendance obj, String newKey) {
		this.redisTemplate.rename(obj.getKey(), newKey);
	}

	@Override
	public void delete(Collection<String> keys) {
		this.redisTemplate.delete(keys);
	}

	@Override
	public void set(Attendance obj) {
		this.redisTemplate.opsForValue().set(obj.getKey(), obj);
	}

	@Override
	public void setIfAbsent(Attendance obj) {
		this.redisTemplate.opsForValue().setIfAbsent(obj.getKey(), obj);
	}

	@Override
	public Attendance get(String key) {
		return this.redisTemplate.opsForValue().get(key);
	}

	@Override
	public Set<String> keys(String pattern) {
		return this.redisTemplate.opsForValue().getOperations().keys(pattern);
	}

	@Override
	public void multiSet(Map<String, Attendance> m) {
		this.redisTemplate.opsForValue().multiSet(m);
	}

}
