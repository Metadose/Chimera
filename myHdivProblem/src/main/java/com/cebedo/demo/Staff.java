package com.cebedo.demo;

public class Staff {

	private long id;
	private String name;

	public Staff() {
		;
	}

	public Staff(long id, String name) {
		setId(id);
		setName(name);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
