package com.cebedo.pmsys.chat.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cebedo.pmsys.system.redis.domain.IDomainObject;
import com.cebedo.pmsys.systemuser.model.SystemUser;

public class Conversation implements IDomainObject {

	private static final long serialVersionUID = 1L;
	private List<SystemUser> contributors;

	public List<SystemUser> getContributors() {
		return contributors;
	}

	public void setContributors(List<SystemUser> contributors) {
		this.contributors = contributors;
	}

	/**
	 * Key: message:conversation:.1.234.56.
	 * 
	 * @param contribs
	 * @return
	 */
	public static String constructKey(List<SystemUser> contribs) {
		Collections.sort(contribs, new Comparator<SystemUser>() {
			@Override
			public int compare(SystemUser aObj, SystemUser bObj) {
				long a = aObj.getId();
				long b = bObj.getId();
				return a < b ? -1 : a > b ? 1 : 0;
			}
		});
		String keyPart = ".";
		for (SystemUser contributor : contribs) {
			keyPart += contributor.getId() + ".";
		}
		String key = Message.OBJECT_NAME + ":conversation:" + keyPart;
		return key;
	}

	private void sortContributors() {
		Collections.sort(this.contributors, new Comparator<SystemUser>() {
			@Override
			public int compare(SystemUser aObj, SystemUser bObj) {
				long a = aObj.getId();
				long b = bObj.getId();
				return a < b ? -1 : a > b ? 1 : 0;
			}
		});
	}

	/**
	 * Key: message:conversation:.1.234.56.
	 */
	@Override
	public String getKey() {
		sortContributors();
		String keyPart = ".";
		for (SystemUser contributor : this.contributors) {
			keyPart += contributor.getId() + ".";
		}
		String key = Message.OBJECT_NAME + ":conversation:" + keyPart;
		return key;
	}

}
