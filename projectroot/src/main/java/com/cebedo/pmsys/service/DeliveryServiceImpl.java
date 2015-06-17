package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.repository.DeliveryValueRepo;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private DeliveryValueRepo deliveryValueRepo;

    public void setDeliveryValueRepo(DeliveryValueRepo deliveryValueRepo) {
	this.deliveryValueRepo = deliveryValueRepo;
    }

    @Override
    @Transactional
    public void rename(Delivery obj, String newKey) {
	this.deliveryValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, Delivery> m) {
	this.deliveryValueRepo.multiSet(m);
    }

    @Override
    @Transactional
    public void set(Delivery obj) {
	this.deliveryValueRepo.set(obj);
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.deliveryValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(Delivery obj) {
	this.deliveryValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public Delivery get(String key) {
	return this.deliveryValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.deliveryValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<Delivery> multiGet(Collection<String> keys) {
	return this.deliveryValueRepo.multiGet(keys);
    }

}
