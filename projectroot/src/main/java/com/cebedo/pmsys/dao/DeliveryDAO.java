package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.DeliveryToDelete;

public interface DeliveryDAO {

    public void create(DeliveryToDelete delivery);

    public DeliveryToDelete getByID(long id);

    public void update(DeliveryToDelete delivery);

    public void delete(long id);

    public List<DeliveryToDelete> list();

}