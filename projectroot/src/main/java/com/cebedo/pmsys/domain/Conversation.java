package com.cebedo.pmsys.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemUser;

public class Conversation implements IDomainObject {

    private static final long serialVersionUID = 1L;
    private Long companyID;
    private List<Long> contributorIDs;
    private boolean read;
    private Map<String, Object> extMap;

    public Long getCompanyID() {
	return companyID;
    }

    public void setCompanyID(Long company) {
	this.companyID = company;
    }

    public boolean isRead() {
	return read;
    }

    public void setRead(boolean read) {
	this.read = read;
    }

    public List<Long> getContributorIDs() {
	return contributorIDs;
    }

    public void setContributorIDs(List<Long> contributors) {
	this.contributorIDs = contributors;
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
		+ RedisConstants.OBJECT_MESSAGE + ":conversation:read:" + rd
		+ ":id:" + keyPart;
	return key;
    }

    private void sortContributors() {
	Collections.sort(this.contributorIDs, new Comparator<Long>() {
	    @Override
	    public int compare(Long a, Long b) {
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
	for (Long contributorID : this.contributorIDs) {
	    keyPart += contributorID + ".";
	}
	long companyID = getCompanyID() == null ? 0 : getCompanyID();
	String key = Company.OBJECT_NAME + ":" + companyID + ":"
		+ RedisConstants.OBJECT_MESSAGE + ":conversation:read:"
		+ isRead() + ":id:" + keyPart;
	return key;
    }

    @Override
    public Map<String, Object> getExtMap() {
	return extMap;
    }

    @Override
    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

}
