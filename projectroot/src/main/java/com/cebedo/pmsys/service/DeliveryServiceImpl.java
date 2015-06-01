package com.cebedo.pmsys.service;

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
import com.cebedo.pmsys.model.Delivery;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private static Logger logger = Logger.getLogger(Delivery.OBJECT_NAME);
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
    public String create(Delivery delivery) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(delivery)) {

	    // Post-service message.
	    this.messageHelper.constructAndSendMessageMap(AuditAction.CREATE,
		    delivery);

	    // Do service.
	    this.deliveryDAO.create(delivery);

	    // Return response.
	    return AlertBoxFactory.SUCCESS.generateCreate(Delivery.OBJECT_NAME,
		    delivery.getName());
	}

	// Log warning.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		Delivery.OBJECT_NAME, delivery.getId(), delivery.getName()));

	// Return failed.
	return AlertBoxFactory.FAILED.generateCreate(Delivery.OBJECT_NAME,
		delivery.getName());
    }

    /**
     * Return an object given an ID.
     */
    @Override
    @Transactional
    public Delivery getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Delivery delivery = this.deliveryDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(delivery)) {
	    // Log the action.
	    logger.info(this.logHelper.logGetObject(auth, Delivery.OBJECT_NAME,
		    id, delivery.getName()));

	    // Return.
	    return delivery;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		Delivery.OBJECT_NAME, delivery.getId(), delivery.getName()));

	// Return empty.
	return new Delivery();
    }

    /**
     * Update a delivery object.
     */
    @Override
    @Transactional
    public String update(Delivery delivery) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(delivery)) {
	    // Log action and notify.
	    this.messageHelper.constructAndSendMessageMap(AuditAction.UPDATE,
		    delivery);

	    // Do action.
	    this.deliveryDAO.update(delivery);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateUpdate(Delivery.OBJECT_NAME,
		    delivery.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		Delivery.OBJECT_NAME, delivery.getId(), delivery.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateUpdate(Delivery.OBJECT_NAME,
		delivery.getName());
    }

    /**
     * Delete a delivery given an id.
     */
    @Override
    @Transactional
    public String delete(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Delivery delivery = this.deliveryDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(delivery)) {
	    // Log action and notify.
	    this.messageHelper.constructAndSendMessageMap(AuditAction.DELETE,
		    delivery);

	    // Do service.
	    this.deliveryDAO.delete(id);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateDelete(Delivery.OBJECT_NAME,
		    delivery.getName());
	}

	// Log warn
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE,
		Delivery.OBJECT_NAME, id, delivery.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateDelete(Delivery.OBJECT_NAME,
		delivery.getName());
    }

    /**
     * List deliveries.
     */
    @Override
    @Transactional
    public List<Delivery> list() {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (auth.isSuperAdmin()) {
	    // Log info.
	    logger.info(this.logHelper.logListAsSuperAdmin(auth,
		    Delivery.OBJECT_NAME));

	    // Return list.
	    return this.deliveryDAO.list();
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorizedSuperAdminOnly(auth,
		AuditAction.LIST, Delivery.OBJECT_NAME));

	// Return empty.
	return new ArrayList<Delivery>();
    }

}
