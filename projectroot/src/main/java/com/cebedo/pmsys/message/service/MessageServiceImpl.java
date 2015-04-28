package com.cebedo.pmsys.message.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.message.domain.Message;
import com.cebedo.pmsys.message.repository.MessageZSetRepo;

@Service
public class MessageServiceImpl implements MessageService {

	private MessageZSetRepo messageZSetRepo;

	public void setMessageZSetRepo(MessageZSetRepo messageZSetRepo) {
		this.messageZSetRepo = messageZSetRepo;
	}

	@Transactional
	@Override
	public void add(Message obj) {
		this.messageZSetRepo.add(obj);
	}

	@Transactional
	@Override
	public Set<Message> rangeByScore(String key, long min, long max) {
		return this.messageZSetRepo.rangeByScore(key, min, max);
	}

	@Transactional
	@Override
	public void removeRangeByScore(String key, long min, long max) {
		this.messageZSetRepo.removeRangeByScore(key, min, max);
	}

}
