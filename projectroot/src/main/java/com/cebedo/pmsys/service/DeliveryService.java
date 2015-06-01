package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.Delivery;

public interface DeliveryService {

    public String create(Delivery delivery);

    public Delivery getByID(long id);

    public String update(Delivery delivery);

    public String delete(long id);

    public List<Delivery> list();

}
