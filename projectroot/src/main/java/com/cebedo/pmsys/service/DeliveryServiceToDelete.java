package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.DeliveryToDelete;

public interface DeliveryServiceToDelete {

    public String create(DeliveryToDelete delivery);

    public DeliveryToDelete getByID(long id);

    public String update(DeliveryToDelete delivery);

    public String delete(long id);

    public List<DeliveryToDelete> list();

}
