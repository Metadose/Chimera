package com.cebedo.pmsys.supply.delivery.model;

import java.util.Set;

import com.cebedo.pmsys.cashflow.expense.model.Expense;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.supply.material.model.Material;
import com.cebedo.pmsys.supply.storage.model.Storage;

public class Delivery {

	private long id;
	private Set<Storage> storage;
	private Set<Staff> staff;
	private Set<Material> materials;
	private Set<Expense> expenses;

}
