package com.cebedo.pmsys.chat.service;

import java.util.Set;

import com.cebedo.pmsys.chat.domain.Conversation;

public interface ConversationService {

	public void add(Conversation obj);

	public Set<Conversation> members(String key);

}
