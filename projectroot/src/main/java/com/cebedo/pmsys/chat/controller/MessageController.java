package com.cebedo.pmsys.chat.controller;

import java.sql.Timestamp;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.cebedo.pmsys.chat.domain.Conversation;
import com.cebedo.pmsys.chat.domain.Message;
import com.cebedo.pmsys.chat.service.ConversationService;
import com.cebedo.pmsys.chat.service.MessageService;
import com.cebedo.pmsys.system.constants.SystemConstants;
import com.cebedo.pmsys.system.helper.AuthHelper;
import com.cebedo.pmsys.systemuser.model.SystemUser;
import com.cebedo.pmsys.systemuser.service.SystemUserService;

@Controller
@SessionAttributes(value = { Message.OBJECT_NAME }, types = { Message.class })
@RequestMapping(MessageController.CONTROLLER_MAPPING)
public class MessageController {

	public static final String CONTROLLER_MAPPING = "message";
	public static final String JSP_LIST = "messageList";
	private AuthHelper authHelper = new AuthHelper();
	private MessageService messageService;
	private SystemUserService systemUserService;
	private ConversationService conversationService;

	@Autowired(required = true)
	@Qualifier(value = "conversationService")
	public void setConversationService(ConversationService s) {
		this.conversationService = s;
	}

	@Autowired(required = true)
	@Qualifier(value = "systemUserService")
	public void setSystemUserService(SystemUserService ps) {
		this.systemUserService = ps;
	}

	@Autowired
	@Qualifier(value = "messageService")
	public void setMessageService(MessageService s) {
		this.messageService = s;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT })
	public String view() {
		return SystemConstants.SYSTEM + "/" + JSP_LIST;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_SEND }, method = RequestMethod.POST)
	public String sendMessage(
			@ModelAttribute(Message.OBJECT_NAME) Message message,
			SessionStatus status) {
		message.setTimestamp(new Timestamp(System.currentTimeMillis()));
		this.messageService.add(message);
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + Message.OBJECT_NAME + "/"
				+ SystemConstants.REQUEST_VIEW + "/"
				+ message.getRecipient().getId();
	}

	@RequestMapping(value = { SystemConstants.REQUEST_VIEW + "/{"
			+ SystemUser.OBJECT_NAME + "}" }, method = RequestMethod.GET)
	public String viewMessages(
			@PathVariable(SystemUser.OBJECT_NAME) long recipientID, Model model) {

		SystemUser user = this.authHelper.getAuth().getUser();

		// Get messages for the last 24 hours.
		Set<Message> messages = this.messageService.rangeByScore(
				Message.constructKey(recipientID, user.getId()),
				System.currentTimeMillis() - 86400000,
				System.currentTimeMillis());

		Message newMessage = new Message();
		newMessage.setSender(user);
		newMessage.setRead(false);
		newMessage.setRecipient(this.systemUserService.getByID(recipientID));

		Set<Conversation> conversations = this.conversationService
				.members(Conversation.constructKey(user.getId()));

		model.addAttribute("messages", messages);
		model.addAttribute("conversations", conversations);
		model.addAttribute(Message.OBJECT_NAME, newMessage);

		return Message.OBJECT_NAME + "/" + JSP_LIST;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_COMPOSE + "/{"
			+ SystemUser.OBJECT_NAME + "}" }, method = RequestMethod.GET)
	public String composeMessage(
			@PathVariable(SystemUser.OBJECT_NAME) long recipientID, Model model) {
		Message newMessage = new Message();

		// Set contributors.
		// List<Long> contributors = new ArrayList<Long>();
		// contributors.add(recipientID);
		// contributors.add(senderID);
		// newMessage.setContributors(contributors);

		// Set read map.
		// Map<Long, Boolean> readMap = new HashMap<Long, Boolean>();
		// readMap.put(senderID, true);
		// newMessage.setReadMap(readMap);

		newMessage.setSender(this.authHelper.getAuth().getUser());
		newMessage.setRead(false);
		newMessage.setRecipient(this.systemUserService.getByID(recipientID));
		// newMessage.setAllRead(false);

		model.addAttribute(Message.OBJECT_NAME, newMessage);

		return Message.OBJECT_NAME + "/compose";
	}
}