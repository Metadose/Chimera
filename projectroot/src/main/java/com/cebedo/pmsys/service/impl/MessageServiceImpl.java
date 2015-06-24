package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.domain.Conversation;
import com.cebedo.pmsys.domain.Message;
import com.cebedo.pmsys.repository.ConversationValueRepo;
import com.cebedo.pmsys.repository.MessageZSetRepo;
import com.cebedo.pmsys.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageZSetRepo messageZSetRepo;
    private ConversationValueRepo conversationValueRepo;

    public void setConversationValueRepo(
	    ConversationValueRepo conversationValueRepo) {
	this.conversationValueRepo = conversationValueRepo;
    }

    public void setMessageZSetRepo(MessageZSetRepo messageZSetRepo) {
	this.messageZSetRepo = messageZSetRepo;
    }

    @Transactional
    @Override
    public void add(Message obj) {
	this.messageZSetRepo.add(obj);

	List<Long> contributors = new ArrayList<Long>();
	contributors.add(obj.getRecipientID());
	contributors.add(obj.getSenderID());

	Conversation converse = new Conversation();
	converse.setCompanyID(obj.getCompanyID());
	converse.setContributorIDs(contributors);
	converse.setRead(false);

	this.conversationValueRepo.setIfAbsent(converse);
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