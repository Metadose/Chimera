package com.cebedo.pmsys.service;

import java.util.Set;

import com.cebedo.pmsys.domain.Message;

public interface MessageService {

	public void add(Message obj);

	public Set<Message> rangeByScore(String key, long min, long max);

	public void removeRangeByScore(String key, long min, long max);

}
