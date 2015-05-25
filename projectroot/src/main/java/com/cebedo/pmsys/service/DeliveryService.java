package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.Delivery;

public interface DeliveryService {

    public void create(Delivery delivery);

    public Delivery getByID(long id);

    public void update(Delivery delivery);

    public void delete(long id);

    public List<Delivery> list();

}
