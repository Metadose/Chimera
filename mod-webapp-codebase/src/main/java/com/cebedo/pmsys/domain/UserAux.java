package com.cebedo.pmsys.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cebedo.pmsys.base.IObjectDomain;
import com.cebedo.pmsys.constants.ConstantsAuthority;
import com.cebedo.pmsys.constants.ConstantsAuthority.AuthorizedAction;
import com.cebedo.pmsys.constants.ConstantsAuthority.AuthorizedModule;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemUser;

public class UserAux implements IObjectDomain {

    private static final long serialVersionUID = -1696724026282355245L;

    private Company company;
    private SystemUser user;
    private Map<AuthorizedModule, List<AuthorizedAction>> authorization = new HashMap<AuthorizedModule, List<AuthorizedAction>>();

    // Form.
    private AuthorizedModule[] modules;
    private AuthorizedAction[] actions;

    public UserAux() {
	;
    }

    public UserAux(SystemUser usr) {
	Company com = usr.getCompany();
	if (com != null) {
	    this.company = com;
	} else {
	    this.company = new Company(0);
	}
	this.user = usr;
    }

    public Collection<GrantedAuthority> getAuthorities(SystemUser user) {
	List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
	for (AuthorizedModule module : this.authorization.keySet()) {
	    List<AuthorizedAction> actions = this.authorization.get(module);
	    for (AuthorizedAction action : actions) {
		String authority = String.format("%s_%s", module, action);
		authList.add(new SimpleGrantedAuthority(authority));
	    }
	}
	if (user.isCompanyAdmin() || user.isSuperAdmin()) {
	    authList.add(new SimpleGrantedAuthority(ConstantsAuthority.ADMIN_COMPANY));
	}
	if (user.isSuperAdmin()) {
	    authList.add(new SimpleGrantedAuthority(ConstantsAuthority.ADMIN_SUPER));
	}
	return authList;
    }

    @Override
    public String getKey() {
	return String.format(RegistryRedisKeys.KEY_AUX_USER, this.company.getId(), this.user.getId());
    }

    public static String constructKey(SystemUser user) {
	Company company = user.getCompany();
	return String.format(RegistryRedisKeys.KEY_AUX_USER, company == null ? 0 : company.getId(),
		user.getId());
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof UserAux ? ((UserAux) obj).getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public SystemUser getUser() {
	return user;
    }

    public void setUser(SystemUser user) {
	this.user = user;
    }

    public AuthorizedModule[] getModules() {
	return modules;
    }

    public void setModules(AuthorizedModule[] modules) {
	this.modules = modules;
    }

    public AuthorizedAction[] getActions() {
	return actions;
    }

    public void setActions(AuthorizedAction[] actions) {
	this.actions = actions;
    }

    public Map<AuthorizedModule, List<AuthorizedAction>> getAuthorization() {
	return authorization;
    }

    public void setAuthorization(Map<AuthorizedModule, List<AuthorizedAction>> authorization) {
	this.authorization = authorization;
    }

    public String toString() {
	return String.format("[%s = %s]", this.user.getId(), this.authorization.toString());
    }

    public void clearFromInput() {
	this.modules = null;
	this.actions = null;
    }

    @Override
    public String getName() {
	return this.user.getUsername();
    }

    @Override
    public String getObjectName() {
	return ConstantsRedis.OBJECT_AUX_USER;
    }

    public UserAux clone() {
	try {
	    return (UserAux) super.clone();
	} catch (Exception e) {
	    return null;
	}
    }
}
