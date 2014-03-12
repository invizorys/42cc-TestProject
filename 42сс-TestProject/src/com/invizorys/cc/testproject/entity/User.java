package com.invizorys.cc.testproject.entity;

public class User {
	private long id;
	private String name;
	private String surname;
	private String birthday;
	private String email;
	
	public User() { }
	
	public User(long id, String name, String surname, String birthday, String email)
	{
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.birthday = birthday;
		this.email = email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
