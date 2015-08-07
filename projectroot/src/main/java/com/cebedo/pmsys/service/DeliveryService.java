package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.model.Project;

public interface DeliveryService {

    public String set(Delivery obj);

    public String delete(String key);

    public Delivery get(String uuid);

    public List<Delivery> list(Project proj);

}
