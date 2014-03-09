package com.invizorys.cc.testproject.entity;

public class User {
	private long id;
	private String name;
	private String surname;
	private String birthday;
	
	public User() { }
	
	public User(long id, String name, String surname, String birthday)
	{
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.birthday = birthday;
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
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

}
