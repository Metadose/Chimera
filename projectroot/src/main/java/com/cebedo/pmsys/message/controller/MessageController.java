package com.cebedo.pmsys.message.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.cebedo.pmsys.message.domain.Message;
import com.cebedo.pmsys.message.repository.MessageZSetRepo;
import com.cebedo.pmsys.system.constants.SystemConstants;
import com.cebedo.pmsys.system.helper.AuthHelper;
import com.cebedo.pmsys.systemuser.model.SystemUser;

@Controller
@SessionAttributes(value = { Message.OBJECT_NAME }, types = { Message.class })
@RequestMapping(MessageController.CONTROLLER_MAPPING)
public class MessageController {

	public static final String CONTROLLER_MAPPING = "message";
	public static final String JSP_LIST = "messageList";
	private AuthHelper authHelper = new AuthHelper();
	private MessageZSetRepo messageZSetRepo;

	@Autowired
	@Qualifier(value = "messageZSetRepo")
	public void setMessageZSetRepo(MessageZSetRepo messageZSetRepo) {
		this.messageZSetRepo = messageZSetRepo;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT })
	public String view() {
		return SystemConstants.SYSTEM + "/" + JSP_LIST;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_SEND }, method = RequestMethod.POST)
	public String sendMessage(
			@ModelAttribute(Message.OBJECT_NAME) Message message) {
		message.setTimestamp(System.currentTimeMillis());
		this.messageZSetRepo.add(message);
		return SystemConstants.SYSTEM + "/" + JSP_LIST;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_COMPOSE + "/{"
			+ SystemUser.OBJECT_NAME + "}" }, method = RequestMethod.GET)
	public String composeMessage(
			@PathVariable(SystemUser.OBJECT_NAME) long recipientID, Model model) {
		long senderID = this.authHelper.getAuth().getUser().getId();
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

		newMessage.setSenderUserID(senderID);
		newMessage.setRead(false);
		newMessage.setRecipientUserID(recipientID);
		// newMessage.setAllRead(false);

		model.addAttribute(Message.OBJECT_NAME, newMessage);

		return Message.OBJECT_NAME + "/compose";
	}
}