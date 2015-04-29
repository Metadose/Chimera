package com.cebedo.pmsys.chat.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.chat.domain.Conversation;
import com.cebedo.pmsys.chat.repository.ConversationSetRepo;

@Service
public class ConversationServiceImpl implements ConversationService {

	private ConversationSetRepo conversationSetRepo;

	public void setConversationSetRepo(ConversationSetRepo conversationSetRepo) {
		this.conversationSetRepo = conversationSetRepo;
	}

	@Transactional
	@Override
	public void add(Conversation obj) {
		this.conversationSetRepo.add(obj);
	}

	@Transactional
	@Override
	public Set<Conversation> members(String key) {
		return this.conversationSetRepo.members(key);
	}

}
