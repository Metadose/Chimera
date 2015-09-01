package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.model.SystemUser;

public interface SystemUserService {

    public void initRoot();

    public List<SystemUser> list();

    /**
     * Create a new system user.
     * 
     * @param user
     * @param result
     * @return
     */
    public String create(SystemUser user, BindingResult result);

    /**
     * Update a user.
     * 
     * @param user
     * @param result
     * @return
     */
    public String update(SystemUser user, BindingResult result);

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

}
