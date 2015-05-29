package com.cebedo.pmsys.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemUser;

public class Conversation implements IDomainObject {

    public static final String OBJECT_NAME = "conversation";
    private static final long serialVersionUID = 1L;
    private Company company;
    private List<SystemUser> contributors;
    private boolean read;

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public boolean isRead() {
	return read;
    }

    public void setRead(boolean read) {
	this.read = read;
    }

    public List<SystemUser> getContributors() {
	return contributors;
    }

    public void setContributors(List<SystemUser> contributors) {
	this.contributors = contributors;
    }

    /**
     * Key: message:conversation:read:true:id:.1.234.56.
     * 
     * @param contribs
     * @return
     */
    public static String constructKey(List<SystemUser> contribs, boolean rd) {
	Collections.sort(contribs, new Comparator<SystemUser>() {
	    @Override
	    public int compare(SystemUser aObj, SystemUser bObj) {
		long a = aObj.getId();
		long b = bObj.getId();
		return a < b ? -1 : a > b ? 1 : 0;
	    }
	});
	String keyPart = ".";
	Company company = null;
	for (SystemUser contributor : contribs) {
	    if (company == null) {
		company = contributor.getCompany();
	    }
	    keyPart += contributor.getId() + ".";
	}
	long companyID = company == null ? 0 : company.getId();
	String key = Company.OBJECT_NAME + ":" + companyID + ":"
		+ Message.OBJECT_NAME + ":conversation:read:" + rd + ":id:"
		+ keyPart;
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
	long companyID = getCompany() == null ? 0 : getCompany().getId();
	String key = Company.OBJECT_NAME + ":" + companyID + ":"
		+ Message.OBJECT_NAME + ":conversation:read:" + isRead()
		+ ":id:" + keyPart;
	return key;
    }

}
