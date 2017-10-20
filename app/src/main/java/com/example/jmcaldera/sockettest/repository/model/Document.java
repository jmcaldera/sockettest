package com.example.jmcaldera.sockettest.repository.model;

import com.google.gson.annotations.SerializedName;

public class Document {

	@SerializedName("precio")
	private int precio;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public void setPrecio(int precio){
		this.precio = precio;
	}

	public int getPrecio(){
		return precio;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"{" +
			"\"precio\": " + precio +
			", \"name\": \"" + name + "\"" +
			", \"id\": " + id +
			"}";
		}
}