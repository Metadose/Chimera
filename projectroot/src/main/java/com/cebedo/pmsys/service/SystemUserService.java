package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.bean.UserSecAccessBean;
import com.cebedo.pmsys.bean.UserSecRoleBean;
import com.cebedo.pmsys.model.SystemUser;

public interface SystemUserService {

    public void initRoot();

    public List<SystemUser> list();

    /**
     * Create a new system user.
     * 
     * @param user
     * @return
     */
    public String create(SystemUser user);

    /**
     * Update a user.
     * 
     * @param user
     * @return
     */
    public String update(SystemUser user);

    /**
     * Update a user.
     * 
     * @param user
     * @param systemOverride
     * @return
     */
    public String update(SystemUser user, boolean systemOverride);

    /**
     * Delete a user given an id.
     * 
     * @param id
     * @return
     */
    public String delete(long id);

    public SystemUser getByID(long id);

    public SystemUser getWithSecurityByID(long id);

    public SystemUser getByID(long id, boolean override);

    public SystemUser searchDatabase(String name);

    /**
     * Assign a security access to a user.
     * 
     * @param user
     * @param secAccBean
     * @return
     */
    public String assignSecurityAccess(SystemUser user,
	    UserSecAccessBean secAccBean);

    /**
     * Unassign a security access from a user.
     * 
     * @param user
     * @param secAccID
     * @return
     */
    public String unassignSecurityAccess(SystemUser user, long secAccID);

    /**
     * Unassign a security role from a user.
     * 
     * @param user
     * @param secRoleID
     * @return
     */
    public String unassignSecurityRole(SystemUser user, long secRoleID);

    /**
     * Unassign all security access.
     * 
     * @param user
     * @return
     */
    public String unassignAllSecurityAccess(SystemUser user);

    /**
     * Unassign all security roles.
     * 
     * @param user
     * @return
     */
    public String unassignAllSecurityRoles(SystemUser user);

    /**
     * Assign a security role to a user.
     * 
     * @param user
     * @param secRoleBean
     * @return
     */
    public String assignSecurityRole(SystemUser user,
	    UserSecRoleBean secRoleBean);

}
