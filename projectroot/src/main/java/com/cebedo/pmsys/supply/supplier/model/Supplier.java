package com.cebedo.pmsys.supply.supplier.model;

import java.util.Set;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.supply.delivery.model.Delivery;

public class Supplier {

	private long id;
	private Set<Project> projects;
	private Set<Delivery> deliveries;

}
