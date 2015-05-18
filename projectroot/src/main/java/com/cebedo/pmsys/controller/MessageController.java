package com.cebedo.pmsys.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.domain.Conversation;
import com.cebedo.pmsys.domain.Message;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.service.ConversationService;
import com.cebedo.pmsys.service.MessageService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.service.SystemUserService;

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
	private StaffService staffService;

	@Autowired(required = true)
	@Qualifier(value = "conversationService")
	public void setConversationService(ConversationService s) {
		this.conversationService = s;
	}

	@Autowired
	@Qualifier(value = "staffService")
	public void setStaffService(StaffService ps) {
		this.staffService = ps;
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

	/**
	 * Send a chat message to a user.
	 * 
	 * @param message
	 * @param status
	 * @return
	 */
	@RequestMapping(value = { SystemConstants.REQUEST_SEND }, method = RequestMethod.POST)
	public String sendMessage(
			@ModelAttribute(Message.OBJECT_NAME) Message message,
			SessionStatus status) {

		// If the recipient is null,
		// then user must have sent this message from
		// an empty form.
		if (message.getRecipient() == null) {
			SystemUser recipient = this.systemUserService.getByID(
					message.getRecipientID(), true);
			message.setRecipient(recipient);
		}
		message.setTimestamp(new Timestamp(System.currentTimeMillis()));
		this.messageService.add(message);
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + Message.OBJECT_NAME + "/"
				+ SystemConstants.REQUEST_VIEW + "/"
				+ message.getRecipient().getId();
	}

	/**
	 * Opens a page with list of conversations and list of messages of the
	 * chosen conversation.
	 * 
	 * @param recipientID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { SystemConstants.REQUEST_VIEW + "/{"
			+ SystemUser.OBJECT_NAME + "}" }, method = RequestMethod.GET)
	public String viewMessages(
			@PathVariable(SystemUser.OBJECT_NAME) long recipientID, Model model) {

		// Get user and create empty containers.
		SystemUser user = this.authHelper.getAuth().getUser();
		Set<Message> messages = new HashSet<Message>();
		Message newMessage = new Message();
		newMessage.setSender(user);
		newMessage.setRead(false);

		// If user has a specific user.
		if (recipientID != 0) {
			// Get the user and
			// get messages from that user
			// since the beginning of time.
			SystemUser recipient = this.systemUserService.getByID(recipientID,
					true);
			newMessage.setRecipient(recipient);
			messages = this.messageService.rangeByScore(
					Message.constructKey(recipientID, user.getId()), 0,
					System.currentTimeMillis());

			// Mark this specific conversation as read.
			List<SystemUser> contribs = new ArrayList<SystemUser>();
			contribs.add(user);
			contribs.add(recipient);
			Conversation conversation = this.conversationService
					.get(Conversation.constructKey(contribs, false));
			if (conversation != null) {
				// FIXME After conversation is created,
				// conversation is automatically read,
				// system can't know if a specific person has read
				// the message.
				this.conversationService.markRead(conversation);
			}
		}

		// Get the chat messages based on user.
		Set<Conversation> conversations = this.conversationService
				.getAllConversations(user);
		model.addAttribute("messages", messages);
		model.addAttribute("staffList", this.staffService.list(user
				.getCompany() == null ? null : user.getCompany().getId()));
		model.addAttribute("conversations", conversations);
		model.addAttribute(Message.OBJECT_NAME, newMessage);

		return Message.OBJECT_NAME + "/" + JSP_LIST;
	}

	/**
	 * Open a page where user can compose message to be sent.
	 * 
	 * @param recipientID
	 * @param model
	 * @return
	 */
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