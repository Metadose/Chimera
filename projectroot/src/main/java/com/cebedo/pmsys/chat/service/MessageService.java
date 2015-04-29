package com.cebedo.pmsys.chat.service;

import java.util.Set;

import com.cebedo.pmsys.chat.domain.Message;

public interface MessageService {

	public void add(Message obj);

	public Set<Message> rangeByScore(String key, long min, long max);

	public void removeRangeByScore(String key, long min, long max);

}
