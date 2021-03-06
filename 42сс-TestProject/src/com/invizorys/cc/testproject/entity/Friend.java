package com.invizorys.cc.testproject.entity;

public class Friend {
	private String id;
	private String name;
	private int priority;

	public Friend() {
	}
	
	public Friend(String id) {
		this.id = id;
	}

	public Friend(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
