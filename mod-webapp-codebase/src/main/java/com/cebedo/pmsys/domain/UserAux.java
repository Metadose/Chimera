package com.cebedo.pmsys.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cebedo.pmsys.constants.ConstantsAuthority.AuthorizedAction;
import com.cebedo.pmsys.constants.ConstantsAuthority.AuthorizedProjectModule;
import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemUser;

public class UserAux implements IDomainObject {

    private static final long serialVersionUID = -1696724026282355245L;

    private Company company;
    private SystemUser user;
    private Map<AuthorizedProjectModule, List<AuthorizedAction>> authorization = new HashMap<AuthorizedProjectModule, List<AuthorizedAction>>();

    // Form.
    private AuthorizedProjectModule[] modules;
    private AuthorizedAction[] actions;

    public UserAux() {
	;
    }

    public UserAux(SystemUser usr) {
	Company com = usr.getCompany();
	if (com != null) {
	    this.company = com;
	}
	this.user = usr;
    }

    public Collection<GrantedAuthority> getAuthorities() {
	List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
	for (AuthorizedProjectModule module : this.authorization.keySet()) {
	    List<AuthorizedAction> actions = this.authorization.get(module);
	    for (AuthorizedAction action : actions) {
		String authority = String.format("%s_%s", module, action);
		authList.add(new SimpleGrantedAuthority(authority));
	    }
	}
	return authList;
    }

    @Override
    public String getKey() {
	return String.format(RegistryRedisKeys.KEY_AUX_USER, this.company.getId(), this.user.getId());
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

    public AuthorizedProjectModule[] getModules() {
	return modules;
    }

    public void setModules(AuthorizedProjectModule[] modules) {
	this.modules = modules;
    }

    public AuthorizedAction[] getActions() {
	return actions;
    }

    public void setActions(AuthorizedAction[] actions) {
	this.actions = actions;
    }

}
