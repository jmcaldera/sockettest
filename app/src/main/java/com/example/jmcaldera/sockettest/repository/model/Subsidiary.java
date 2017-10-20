package com.example.jmcaldera.sockettest.repository.model;

import com.google.gson.annotations.SerializedName;

public class Subsidiary{

	@SerializedName("id")
	private int id;

	@SerializedName("name")
	private String name;

	@SerializedName("address")
	private String address;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	@Override
 	public String toString(){
		return 
			"{" +
			"\"address\": \"" + address + "\"" +
			", \"name\": \"" + name + "\"" +
			", \"id\": " + id +
			"}";
		}
}