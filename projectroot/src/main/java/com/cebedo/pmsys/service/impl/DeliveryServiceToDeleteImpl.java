package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.DeliveryDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.DeliveryToDelete;
import com.cebedo.pmsys.service.DeliveryServiceToDelete;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class DeliveryServiceToDeleteImpl implements DeliveryServiceToDelete {

    private static Logger logger = Logger.getLogger(DeliveryToDelete.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private DeliveryDAO deliveryDAO;

    public void setDeliveryDAO(DeliveryDAO deliveryDAO) {
	this.deliveryDAO = deliveryDAO;
    }

    /**
     * Create a new delivery.
     */
    @Override
    @Transactional
    public String create(DeliveryToDelete delivery) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(delivery)) {

	    // Post-service message.
	    this.messageHelper.sendAction(AuditAction.CREATE,
		    delivery);

	    // Do service.
	    this.deliveryDAO.create(delivery);

	    // Return response.
	    return AlertBoxGenerator.SUCCESS.generateCreate(DeliveryToDelete.OBJECT_NAME,
		    delivery.getName());
	}

	// Log warning.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		DeliveryToDelete.OBJECT_NAME, delivery.getId(), delivery.getName()));

	// Return failed.
	return AlertBoxGenerator.FAILED.generateCreate(DeliveryToDelete.OBJECT_NAME,
		delivery.getName());
    }

    /**
     * Return an object given an ID.
     */
    @Override
    @Transactional
    public DeliveryToDelete getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	DeliveryToDelete delivery = this.deliveryDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(delivery)) {
	    // Log the action.
	    logger.info(this.logHelper.logGetObject(auth, DeliveryToDelete.OBJECT_NAME,
		    id, delivery.getName()));

	    // Return.
	    return delivery;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		DeliveryToDelete.OBJECT_NAME, delivery.getId(), delivery.getName()));

	// Return empty.
	return new DeliveryToDelete();
    }

    /**
     * Update a delivery object.
     */
    @Override
    @Transactional
    public String update(DeliveryToDelete delivery) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(delivery)) {
	    // Log action and notify.
	    this.messageHelper.sendAction(AuditAction.UPDATE,
		    delivery);

	    // Do action.
	    this.deliveryDAO.update(delivery);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(DeliveryToDelete.OBJECT_NAME,
		    delivery.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		DeliveryToDelete.OBJECT_NAME, delivery.getId(), delivery.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(DeliveryToDelete.OBJECT_NAME,
		delivery.getName());
    }

    /**
     * Delete a delivery given an id.
     */
    @Override
    @Transactional
    public String delete(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	DeliveryToDelete delivery = this.deliveryDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(delivery)) {
	    // Log action and notify.
	    this.messageHelper.sendAction(AuditAction.DELETE,
		    delivery);

	    // Do service.
	    this.deliveryDAO.delete(id);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateDelete(DeliveryToDelete.OBJECT_NAME,
		    delivery.getName());
	}

	// Log warn
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE,
		DeliveryToDelete.OBJECT_NAME, id, delivery.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateDelete(DeliveryToDelete.OBJECT_NAME,
		delivery.getName());
    }

    /**
     * List deliveries.
     */
    @Override
    @Transactional
    public List<DeliveryToDelete> list() {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (auth.isSuperAdmin()) {
	    // Log info.
	    logger.info(this.logHelper.logListAsSuperAdmin(auth,
		    DeliveryToDelete.OBJECT_NAME));

	    // Return list.
	    return this.deliveryDAO.list();
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorizedSuperAdminOnly(auth,
		AuditAction.LIST, DeliveryToDelete.OBJECT_NAME));

	// Return empty.
	return new ArrayList<DeliveryToDelete>();
    }

}
