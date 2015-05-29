package com.cebedo.pmsys.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.DeliveryDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Delivery;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private static Logger logger = Logger.getLogger(Delivery.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private DeliveryDAO deliveryDAO;

    public void setDeliveryDAO(DeliveryDAO deliveryDAO) {
	this.deliveryDAO = deliveryDAO;
    }

    @Override
    @Transactional
    public void create(Delivery delivery) {
	// TODO
	if (this.authHelper.isActionAuthorized(delivery)) {
	    this.messageHelper.constructAndSendMessageMap(AuditAction.CREATE,
		    delivery);
	    this.deliveryDAO.create(delivery);
	    return;
	}
    }

    @Override
    @Transactional
    public Delivery getByID(long id) {
	Delivery delivery = this.deliveryDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(delivery)) {
	    return delivery;
	}
	return new Delivery();
    }

    @Override
    @Transactional
    public void update(Delivery delivery) {
	if (this.authHelper.isActionAuthorized(delivery)) {
	    this.deliveryDAO.update(delivery);
	}
    }

    @Override
    @Transactional
    public void delete(long id) {
	Delivery delivery = this.deliveryDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(delivery)) {
	    this.deliveryDAO.delete(id);
	}
    }

    @Override
    @Transactional
    public List<Delivery> list() {
	return this.deliveryDAO.list();
    }

}
