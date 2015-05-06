package com.cebedo.pmsys.chat.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.chat.domain.Conversation;
import com.cebedo.pmsys.chat.domain.Message;
import com.cebedo.pmsys.chat.repository.ConversationValueRepo;
import com.cebedo.pmsys.systemuser.model.SystemUser;

@Service
public class ConversationServiceImpl implements ConversationService {

	private ConversationValueRepo conversationValueRepo;

	public void setConversationValueRepo(ConversationValueRepo repo) {
		this.conversationValueRepo = repo;
	}

	@Transactional
	@Override
	public void markRead(Conversation obj) {
		// If object is read, not need to mark it.
		if (!obj.isRead()) {

			// Else, rename it.
			String readKey = Conversation.constructKey(obj.getContributors(),
					true);
			this.conversationValueRepo.rename(obj, readKey);

			// Using the new renamed key,
			// update the object.
			obj.setRead(true);
			this.conversationValueRepo.set(obj);
		}
	}

	@Transactional
	@Override
	public void set(Conversation obj) {
		this.conversationValueRepo.set(obj);
	}

	@Transactional
	@Override
	public Conversation get(String key) {
		return this.conversationValueRepo.get(key);
	}

	@Transactional
	@Override
	public Set<String> keys(String pattern) {
		return this.conversationValueRepo.keys(pattern);
	}

	/**
	 * Get all conversations of a specific user.
	 */
	@Transactional
	@Override
	public Set<Conversation> getAllConversations(SystemUser user) {
		// message:conversation:read:true:id:.1.234.56.
		String pattern = Message.OBJECT_NAME + ":conversation:read:*:id:*."
				+ user.getId() + ".*";
		Set<String> keys = this.conversationValueRepo.keys(pattern);
		Set<Conversation> conversations = new HashSet<Conversation>();
		for (String key : keys) {
			Conversation conversation = this.conversationValueRepo.get(key);
			conversations.add(conversation);
		}
		return conversations;
	}

}
