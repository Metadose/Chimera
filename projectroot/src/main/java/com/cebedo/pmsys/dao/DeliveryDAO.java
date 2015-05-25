package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Delivery;

public interface DeliveryDAO {

    public void create(Delivery delivery);

    public Delivery getByID(long id);

    public void update(Delivery delivery);

    public void delete(long id);

    public List<Delivery> list();

}