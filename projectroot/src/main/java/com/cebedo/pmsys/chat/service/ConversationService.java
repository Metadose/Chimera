package com.cebedo.pmsys.chat.service;

import java.util.Set;

import com.cebedo.pmsys.chat.domain.Conversation;
import com.cebedo.pmsys.systemuser.model.SystemUser;

public interface ConversationService {

	public void markRead(Conversation obj);

	public void set(Conversation obj);

	public Conversation get(String key);

	public Set<String> keys(String pattern);

	public Set<Conversation> getAllConversations(SystemUser user);

}
